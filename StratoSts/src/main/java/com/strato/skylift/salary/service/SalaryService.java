package com.strato.skylift.salary.service;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.SalaryStatement;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.salary.dto.AttendanceDTO;
import com.strato.skylift.salary.dto.SalMemberDTO;
import com.strato.skylift.salary.dto.SalaryDTO;
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

       memberList.forEach(m -> log.info("member : {}", m));
        for(int i = 0 ; i < memberList.size(); i++)
            memberList.get(i).getFiles().get(i).setFilePath(IMAGE_URL + "member/" + memberList.get(i).getFiles().get(i).getFilePath());
        return memberList.stream().map(m -> modelMapper.map(m, MbMemberDto.class)).collect(Collectors.toList());
    }

    public SalaryDTO findByWork(Long memberCode, String day , int page)
    {
        String newDay = day.substring(0,7) + "-02";
        Pageable pageable = PageRequest.of(page - 1, 10 , Sort.by("attendanceCode").ascending());
        Page<Attendance> attendance = salAttendanceRepository.findByMemeberCodeLikeDay(memberCode,newDay, pageable);
        Member member = slMemberRepository.findById(memberCode).orElseThrow(() -> new IllegalArgumentException("코드의 해당 상품 없다. : " + memberCode));
        Page<AttendanceDTO> attendanceDTO = attendance.map(m -> modelMapper.map(m , AttendanceDTO.class));

       attendanceDTO.forEach(a -> log.info("attendanceDTO : {}" , a));

       attendanceDTO.forEach(a -> a.setWorkTime(calculator.Conversion(a)));
       attendanceDTO.forEach(a -> a.setOverWorkTime(calculator.overTime(a.getWorkTime())));

       attendanceDTO.forEach(a -> log.info("attendanceDTO : {} ", a));
       SalaryDTO salaryDTO = new SalaryDTO();
       salaryDTO.setAttendance(attendanceDTO.getContent());
       salaryDTO.setPageInfo(Pagenation.getPagingButtonInfo(attendanceDTO));
       salaryDTO.setTotalTime(calculator.totalTime(attendanceDTO.getContent()));
       salaryDTO.setOverTime(calculator.totalOverTime(attendanceDTO.getContent()));

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
        Long income = calculator.income(salaryDTO.getMember().getMemberSalary());
        Long nationalPesion = calculator.nationalPension(salaryDTO.getMember().getMemberSalary());
        Long medicalInsurance = calculator.medicalInsurance(salaryDTO.getMember().getMemberSalary());
        Long employmentInsurance = calculator.empInsurance(salaryDTO.getMember().getMemberSalary());
        Long totalAmount = salaryDTO.getMember().getMemberSalary() + allowance;
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
}
