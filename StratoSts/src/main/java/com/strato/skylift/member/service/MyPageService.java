package com.strato.skylift.member.service;

import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import com.strato.skylift.exception.UserNotFoundException;
import com.strato.skylift.member.dto.MbAttendanceDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.AttendanceRepository;
import com.strato.skylift.member.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final ModelMapper modelMapper;
    private final AttendanceRepository attendanceRepository;

    public Page<MbMemberDto> selectMemberList(int page) {

        Pageable pageable = PageRequest.of(page -1, 10, Sort.by("memberCode").descending());
        Page<Member> memberList = myPageRepository.findAll(pageable);
        Page<MbMemberDto> mbMemberDto = memberList.map(member -> modelMapper.map(member, MbMemberDto.class));

        return mbMemberDto;
    }

    public MbMemberDto findByMemberCode(Long memberCode) {
        log.info("[MyPageService] memberCode : {}", memberCode);

        Member member = myPageRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("조회하는 직원이 없습니다. memberCode=" + memberCode));
        MbMemberDto mbMemberDto = modelMapper.map(member, MbMemberDto.class);

        return mbMemberDto;
    }


    public List<MbAttendanceDto> getAttendanceByMemberCode(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException("조회하는 직원이 없습니다. memberCode=" + memberCode);
        }

        Member member = memberOptional.get();
        Optional<Attendance> attendanceList = attendanceRepository.findAllByMemberMemberCode(memberCode);
        List<MbAttendanceDto> mbAttendanceList = attendanceList.stream()
                .map(attendance -> {
                    MbAttendanceDto mbAttendanceDto = modelMapper.map(attendance, MbAttendanceDto.class);
                    log.info("[MyPageService] Member Code: {}, Date: {}, Status: {}", memberCode, mbAttendanceDto.getAttendanceDate(), mbAttendanceDto.getStatus());
                    return mbAttendanceDto;
                })
                .collect(Collectors.toList());
        return mbAttendanceList;
    }




    @Transactional
    public MbMemberDto updateMember(MbMemberDto authenticatedMemberDto, MbMemberDto updatedMemberDto) {
        Member originMember = myPageRepository.findByMemberCode(authenticatedMemberDto.getMemberCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. memberCode=" + authenticatedMemberDto.getMemberCode()));

        if (updatedMemberDto.getBankName() != null) {
            originMember.setBankName(updatedMemberDto.getBankName());
        }

        if (updatedMemberDto.getAddress() != null) {
            originMember.setAddress(updatedMemberDto.getAddress());
        }

        if (updatedMemberDto.getPhone() != null) {
            originMember.setPhone(updatedMemberDto.getPhone());
        }

        if (updatedMemberDto.getBankNo() != null) {
            originMember.setBankNo(updatedMemberDto.getBankNo());
        }

        Member updatedMember = myPageRepository.save(originMember); // 변경된 필드를 데이터베이스에 저장

        return convertToDto(updatedMember);
    }




    private MbMemberDto convertToDto(Member member) {
        MbMemberDto dto = new MbMemberDto();
        dto.setMemberCode(member.getMemberCode());
        dto.setBankName(member.getBankName());
        dto.setAddress(member.getAddress());
        dto.setPhone(member.getPhone());
        dto.setBankNo(member.getBankNo());
        return dto;
    }

//오늘 출근하고 출근 되면 오류뜨게하기
    @Transactional
    public void manageAttendance(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // Get 'today' date without time detail
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date today = calendar.getTime();

            // Find the latest attendance record for the member
            List<Attendance> latestAttendanceList = attendanceRepository.findLatestAttendanceByMember(member);
            if (!latestAttendanceList.isEmpty()) {
                Attendance latestAttendance = latestAttendanceList.get(0);

                Date lastAttendanceDate = latestAttendance.getAttendanceDate();
                if (lastAttendanceDate != null && lastAttendanceDate.compareTo(today) == 0) {
                    throw new IllegalStateException("오늘 이미 출근하셨습니다. 더 이상 출근이 불가능합니다.");
                }
            }

            // Find today's attendance record
            List<Attendance> todayAttendances = attendanceRepository.findByAttendanceDateAndMember(today, member);

            // If there is no attendance record for today, create and save new attendance
            if (todayAttendances.isEmpty()) {
                Attendance newAttendance = new Attendance();
                newAttendance.setMember(member);
                newAttendance.setStatus("출근");
                newAttendance.setAttendanceDate(today);
                newAttendance.setStartTime(new Date()); // The actual start time is now
                attendanceRepository.save(newAttendance);
                log.info("출근 기록: Member Id - {}, Attendance Date - {}, Start Time - {}",
                        member.getMemberId(),
                        newAttendance.getAttendanceDate(),
                        newAttendance.getStartTime());
            } else {
                throw new IllegalStateException("오늘 이미 출근하셨습니다. 더 이상 출근이 불가능합니다.");
            }
        }
    }




    /* 회원 근태 퇴근 기록하기 */
    @Transactional
    public void manageAttendanceEndChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // 가장 최근의 출석 기록을 찾음
            Optional<Attendance> attendanceOptional = attendanceRepository.findTopByMemberOrderByAttendanceDateDesc(member);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 오늘 날짜에 이미 퇴근한 경우
                if (isToday(attendance.getAttendanceDate()) && "퇴근".equals(attendance.getStatus())) {
                    throw new IllegalStateException("오늘 이미 퇴근하셨습니다. 더 이상 퇴근이 불가능합니다.");
                }

                // 퇴근 버튼을 누를 때 퇴근 시간만 업데이트하고 외출, 복귀 시간은 null로 유지 그리고 출근 시간은 그대로 두기
                if (isToday(attendance.getAttendanceDate())) {
                    attendance.setEndTime(new Date()); // 퇴근 시간 업데이트
                    attendance.setStatus("퇴근"); // 출석 상태를 "퇴근"으로 변경
                    attendanceRepository.save(attendance); // 출석 정보 업데이트
                }
            } else {
                // 출석 기록이 없을 경우
                throw new IllegalArgumentException("먼저 출석을 하세요!");
            }
        }
    }



    /* 회원 근태 외출 기록하기 */
    @Transactional
    public void manageAttendanceOutChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date today = calendar.getTime();
            Optional<Attendance> attendanceOptional = attendanceRepository.findByMemberMemberCodeAndAttendanceDate(memberCode, today);

            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 출근한 상태인지 체크
                if (attendance.getStatus().equals("출근")) {
                    // 외출 버튼을 누를 때 외출 시간만 업데이트하고 복귀 시간은 null로 유지
                    if (isToday(attendance.getAttendanceDate())) {
                        // 이미 외출한 경우 예외 처리
                        if (attendance.getOutTime() != null) {
                            throw new RuntimeException("이미 외출하셨으므로 다시 외출은 불가능합니다.");
                        }

                        attendance.setOutTime(new Date()); // 외출 시간 업데이트
                        attendance.setReturnTime(null); // 복귀 시간 null로 설정
                        attendance.setStatus("외출"); // 출석 상태를 "외출"로 변경
                        attendanceRepository.save(attendance); // 출근 정보 업데이트
                        return; // 메소드 종료
                    } else {
                        throw new RuntimeException("출근일자와 오늘 날짜가 일치하지 않습니다.");
                    }
                } else {
                    // 출근을 먼저 해주세요.
                    throw new RuntimeException("출근을 먼저 해주세요.");
                }
            } else {
                // 출근을 안눌렀을 경우
                throw new RuntimeException("출근을 먼저 해주세요.");
            }
        }
        // 로그인을 다시 확인하세요.
        throw new RuntimeException("로그인을 다시 확인하세요.");
    }


    /* 회원 근태 복귀 기록하기 */
    @Transactional
    public void manageAttendanceReturnChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // 변경: 최신 출석 기록을 가져오는 로직 수정
            Optional<Attendance> attendanceOptional = attendanceRepository.findTopByMemberOrderByAttendanceDateDesc(member);

            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 외출한 상태인지 체크
                if (attendance.getStatus().equals("외출")) {
                    if (attendance.getOutTime() != null && attendance.getReturnTime() == null) {
                        // 외출 시간이 기록되어 있고, 복귀 시간이 아직 기록되지 않은 경우에만 복귀 가능
                        attendance.setReturnTime(new Date()); // 복귀 시간 업데이트
                        attendance.setStatus("복귀"); // 출석 상태를 "복귀"로 변경
                        attendanceRepository.save(attendance); // 출근 정보 업데이트
                    } else {
                        throw new RuntimeException("외출 후 복귀를 눌러주세요.");
                    }
                } else {
                    // 외출을 먼저 해주세요.
                    throw new RuntimeException("외출을 먼저 해주세요.");
                }
            } else {
                // 출근을 안눌렀을 경우
                throw new RuntimeException("출근을 먼저 해주세요.");
            }
        } else {
            throw new RuntimeException("로그인을 다시 확인하세요.");
        }
    }








    //isToday 정의하기
    private boolean isToday(Date date) {
        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        int yearToday = calendarToday.get(Calendar.YEAR);
        int monthToday = calendarToday.get(Calendar.MONTH);
        int dayToday = calendarToday.get(Calendar.DAY_OF_MONTH);

        int yearDate = calendarDate.get(Calendar.YEAR);
        int monthDate = calendarDate.get(Calendar.MONTH);
        int dayDate = calendarDate.get(Calendar.DAY_OF_MONTH);

        return (yearToday == yearDate && monthToday == monthDate && dayToday == dayDate);
    }


    public long getTotalMemberCount() {
        return myPageRepository.count(); // MyPageRepository는 JPA Repository 인터페이스
    }

    public MbMemberDto selectMyInfo(Long memberCode) {

        log.info("멤버서비스: {}", memberCode  );
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new UserNotFoundException(memberCode + "를 찾을 수 없습니다"));

        return  modelMapper.map(member,MbMemberDto.class);
    }


    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    //오늘 해당하는 출근만 조회하기


//    public List<Attendance> getTodayAttendances() {
//        LocalDate localDate = LocalDate.now();
//        Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date endOfDay = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        log.info("Finding attendance for date: {}", localDate);
//        List<Attendance> attendances = attendanceRepository.findByAttendanceDateBetween(startOfDay, endOfDay);
//        log.info("Found {} attendance records for today", attendances.size());
//        return attendances;
//    }



    //
    @Transactional(readOnly = true)
    public String findEmailByName(String name) {
        Member member = myPageRepository.findByMemberName(name);
        if (member != null) {
            return "Name: " + member.getMemberName() + ", Email: " + member.getMemberId();
        }
        throw new RuntimeException("Member not found");
    }


    public List<Attendance> getTodayAttendances() {
        LocalDate localDate = LocalDate.now();
        Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        log.info("Finding attendance for date: {}", localDate);
        List<Attendance> attendances = attendanceRepository.findByAttendanceDateBetween(startOfDay, endOfDay);
        log.info("Found {} attendance records for today", attendances.size());
        return attendances;
    }
}
