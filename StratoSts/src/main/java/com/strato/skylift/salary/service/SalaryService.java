package com.strato.skylift.salary.service;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.SalaryStatement;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.salary.dto.AttendanceDTO;
import com.strato.skylift.salary.dto.SalMemberDTO;
import com.strato.skylift.salary.dto.SalaryDTO;
import com.strato.skylift.salary.dto.SalaryStatementDTO;
import com.strato.skylift.salary.repository.SalAttendanceRepository;
import com.strato.skylift.salary.repository.SLMemberRepository;
import com.strato.skylift.salary.repository.SalaryRepository;
import com.strato.skylift.salary.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SalaryService
{
    private SalaryRepository salaryRepository;
    private SLMemberRepository slMemberRepository;
    private SalAttendanceRepository salAttendanceRepository;
    private ModelMapper modelMapper;
    private Calculator calculator = new Calculator();

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public SalaryService(SalaryRepository salaryRepository,
                         SLMemberRepository slMemberRepository,
                         SalAttendanceRepository salAttendanceRepository,
                         ModelMapper modelMapper)
    {
        this.salaryRepository = salaryRepository;
        this.slMemberRepository = slMemberRepository;
        this.salAttendanceRepository = salAttendanceRepository;
        this.modelMapper = modelMapper;
    }

    public Page<SalaryDTO> findByMemberCode(MbMemberDto member , int page)
    {
        Pageable pageable = PageRequest.of(page -1 , 12 , Sort.by("salaryCode").descending());
        log.info("member : {}" , member);
        Page<SalaryStatement> salaryStatements = salaryRepository.findByMemberMemberId(member.getMemberCode(),pageable);

        Page<SalaryDTO> salaryDTO = salaryStatements.map(sal -> modelMapper.map(sal,SalaryDTO.class));

        salaryDTO.getContent().forEach(s -> s.setSalaryDay(s.getSalaleDate()));

        return salaryDTO;
    }

    public List<MbMemberDto> findByMemberNameLike(String value)
    {
        List<Member> memberList = slMemberRepository.findByMemberNameLike(value);
        List<MbMemberDto> memberDTO =  memberList.stream().map(m -> modelMapper.map(m, MbMemberDto.class)).collect(Collectors.toList());
        memberDTO.forEach(m -> log.info("member : {}", m.getFiles()));
       log.info("size : {}" , memberDTO.size());

        for(int i = 0 ; i < memberDTO.size(); i++)
        {
            memberDTO.get(i).getFiles().get(0).setFilePath(IMAGE_URL + "member/" + memberDTO.get(i).getFiles().get(0).getFilePath());
        }
        return memberDTO;
    }

    public SalaryDTO findByWork(Long memberCode, String day , int page)
    {
        String newDay = day.substring(0,7) + "-01";
        log.info("newDay : {}", newDay);
        Pageable pageable = PageRequest.of(page - 1, 10 , Sort.by("attendanceCode").ascending());
        Page<Attendance> attendance = salAttendanceRepository.findByMemeberCodeLikeDay(memberCode,newDay, pageable);
        List<Attendance> attDay = salAttendanceRepository.findByDay(memberCode,newDay);
        Member member = slMemberRepository.findById(memberCode).orElseThrow(() -> new IllegalArgumentException("코드의 해당 직원 없다. : " + memberCode));
        Page<AttendanceDTO> attendanceDTO = attendance.map(m -> modelMapper.map(m , AttendanceDTO.class));
        List<AttendanceDTO> attDayDTO = attDay.stream().map(m -> modelMapper.map(m, AttendanceDTO.class)).collect(Collectors.toList());

        attDayDTO.forEach(a -> a.setWorkTime(calculator.Conversion(a)));
        attDayDTO.forEach(a -> a.setOverWorkTime(calculator.overTime(a.getWorkTime())));
        attendanceDTO.forEach(a -> a.setWorkTime(calculator.Conversion(a)));
        attendanceDTO.forEach(a -> a.setOverWorkTime(calculator.overTime(a.getWorkTime())));

       attendanceDTO.forEach(a -> log.info("attendanceDTO : {} ", a));
        attDayDTO.forEach(a -> log.info("attDTO : {} " , a));

       SalaryDTO salaryDTO = new SalaryDTO();
       salaryDTO.setAttendance(attendanceDTO.getContent());
       salaryDTO.setPageInfo(Pagenation.getPagingButtonInfo(attendanceDTO));
       salaryDTO.setTotalTime(calculator.totalTime(attDayDTO));
       salaryDTO.setOverTime(calculator.totalOverTime(attDayDTO));

       salaryDTO.setMember(modelMapper.map(member,MbMemberDto.class));

       Long late = salAttendanceRepository.countByLate(memberCode,newDay);
       Long out = salAttendanceRepository.countByOut(memberCode,newDay);
       Long earlyLeave = salAttendanceRepository.countByearlyLeave(memberCode,newDay);
       Long absence = salAttendanceRepository.countByabsence(memberCode,newDay);

       salaryDTO.setLate(late == null ? 0L : late);
       salaryDTO.setOut(out == null ? 0L : out);
       salaryDTO.setEarlyLeave(earlyLeave == null ? 0L : earlyLeave);
       salaryDTO.setAbsence(absence == null ? 0L : absence);

       // 계산기 클래스에서 급여부분 부터 하면 됌 그러면 내일 프론트 랑 연결해서 쓰면 됄듯함
        Long allowance = calculator.allowance(salaryDTO.getMember().getMemberSalary() , salaryDTO.getOverTime());
        Long totalAmount = salaryDTO.getMember().getMemberSalary() + allowance;
        Long income = calculator.income(totalAmount);
        Long nationalPesion = calculator.nationalPension(totalAmount);
        Long medicalInsurance = calculator.medicalInsurance(totalAmount);
        Long employmentInsurance = calculator.empInsurance(totalAmount);
        Long totalDeducted = income + nationalPesion + medicalInsurance + employmentInsurance;
        Long paymentAmount = totalAmount - totalDeducted;

        salaryDTO.setAllowance(allowance);
        salaryDTO.setIncomeTax(income);
        salaryDTO.setNationalPesion(nationalPesion);
        salaryDTO.setMedicalInsurance(medicalInsurance);
        salaryDTO.setEmploymentInsurance(employmentInsurance);
        salaryDTO.setTotalAmount(totalAmount);
        salaryDTO.setTotalDeducted(totalDeducted);
        salaryDTO.setPaymentAmount(paymentAmount);
        salaryDTO.setSalaleDate(new Date());

        log.info("salaryDTO : {}" , salaryDTO);
       return salaryDTO;

    }

    @Transactional
    public void registSal(SalaryStatementDTO salaryDTO)
    {
        SalaryStatement salaryStatement = modelMapper.map(salaryDTO , SalaryStatement.class);
        salaryStatement.setSalaleDate(new Date());
        salaryStatement.setMember(slMemberRepository.findById(salaryDTO.getMemberCode())
                .orElseThrow(() -> new IllegalArgumentException("코드의 해당 직원 없다. : ")));

        salaryRepository.save(salaryStatement);
    }

    public SalaryDTO reIncome(Long salary ,Long overTime)
    {
        SalaryDTO salaryDTO = new SalaryDTO();
        Long allowance = calculator.allowance(salary , overTime);
        Long totalAmount = salary + allowance;
        Long income = calculator.income(totalAmount);
        Long nationalPesion = calculator.nationalPension(totalAmount);
        Long medicalInsurance = calculator.medicalInsurance(totalAmount);
        Long employmentInsurance = calculator.empInsurance(totalAmount);
        Long totalDeducted = income + nationalPesion + medicalInsurance + employmentInsurance;
        Long paymentAmount = totalAmount - totalDeducted;

        salaryDTO.setSalary(salary);
        salaryDTO.setAllowance(allowance);
        salaryDTO.setIncomeTax(income);
        salaryDTO.setNationalPesion(nationalPesion);
        salaryDTO.setMedicalInsurance(medicalInsurance);
        salaryDTO.setEmploymentInsurance(employmentInsurance);
        salaryDTO.setTotalAmount(totalAmount);
        salaryDTO.setTotalDeducted(totalDeducted);
        salaryDTO.setPaymentAmount(paymentAmount);

        return salaryDTO;
    }
}
