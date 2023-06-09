package com.strato.skylift.member.service;

import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


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

    /* 회원 정보 수정하기 */
    @Transactional
    public void updateMember(MbMemberDto mbMemberDto) {

            Member originMember = myPageRepository.findByMemberCode(mbMemberDto.getMemberCode())
                    .orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. memberCode=" + mbMemberDto.getMemberCode()));

            originMember.update(
                    mbMemberDto.getBankName(),
                    mbMemberDto.getAddress(),
                    mbMemberDto.getPhone(),
                    mbMemberDto.getBankNo(),
                    mbMemberDto.getMemberStatus()
            );

    }


    /* 회원 근태 출석 기록하기 */
    @Transactional
    public void manageAttendance(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 출근 버튼을 누를 때 출근 시간만 업데이트하고 퇴근, 외출, 복귀 시간은 null로 유지
                if (isToday(attendance.getAttendanceDate())) {
                    attendance.setStartTime(new Date()); // 출근 시간 업데이트
                    attendance.setEndTime(null); // 퇴근 시간 null로 설정
                    attendance.setOutTime(null); // 외출 시간 null로 설정
                    attendance.setReturnTime(null); // 복귀 시간 null로 설정
                    attendanceRepository.save(attendance); // 출근 정보 업데이트
                }
            } else {
                // 처음 출근하는 경우
                Attendance newAttendance = new Attendance();
                newAttendance.setMember(member);
                newAttendance.setStatus("출근");
                newAttendance.setAttendanceDate(new Date());
                newAttendance.setStartTime(new Date());
                attendanceRepository.save(newAttendance); // 출근 정보 저장
            }
        }
    }

    /* 회원 근태 퇴근 기록하기 */
    @Transactional
    public void manageAttendanceEndChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 퇴근 버튼을 누를 때 퇴근 시간만 업데이트하고 외출, 복귀 시간은 null로 유지 그리고 출근 시간은 그대로 두기
                if (isToday(attendance.getAttendanceDate())) {
                    attendance.setEndTime(new Date()); // 퇴근 시간 업데이트
                    attendance.setStatus("퇴근"); // 출석 상태를 "퇴근"으로 변경
                    attendanceRepository.save(attendance); // 출근 정보 업데이트

                }
            } else {
                // 출근을 안눌렀을 경우
                Attendance newAttendance = new Attendance();
                newAttendance.setMember(member);
                newAttendance.setStatus("출근");
                newAttendance.setAttendanceDate(new Date());
                newAttendance.setStartTime(new Date());
                attendanceRepository.save(newAttendance); // 출근 정보 저장
            }
        }
    }

    /* 회원 근태 외출 기록하기 */
    @Transactional
    public void manageAttendanceOutChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
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
                Attendance newAttendance = new Attendance();
                newAttendance.setMember(member);
                newAttendance.setStatus("출근"); // 출석 상태를 "출근"으로 설정
                newAttendance.setAttendanceDate(new Date());
                newAttendance.setStartTime(new Date());
                attendanceRepository.save(newAttendance); // 출근 정보 저장
                return; // 메소드 종료
            }
        }
        // 로그인을 다시 확인하세요.
        throw new RuntimeException("로그인을 다시 확인하세요.");
    }


    /* 회원 근태 복귀 기록하기 */
    @Transactional
    public void manageAttendanceRetrunChange(Long memberCode) {
        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 출근한 상태인지 체크
                if (attendance.getStatus().equals("출근")) {
                    if (attendance.getOutTime() != null && attendance.getReturnTime() == null) {
                        // 출근 및 외출이 동시에 기록된 경우에만 복귀 가능
                        attendance.setReturnTime(new Date()); // 복귀 시간 업데이트
                        attendance.setStatus("복귀"); // 출석 상태를 "복귀"로 변경
                        attendanceRepository.save(attendance); // 출근 정보 업데이트
                    } else {
                        throw new RuntimeException("출근 후 외출을 먼저 해주세요.");
                    }
                } else {
                    // 출근을 먼저 해주세요.
                    throw new RuntimeException("출근을 먼저 해주세요.");
                }
            } else {
                // 출근을 안눌렀을 경우
                Attendance newAttendance = new Attendance();
                newAttendance.setMember(member);
                newAttendance.setStatus("출근"); // 출석 상태를 "출근"으로 설정
                newAttendance.setAttendanceDate(new Date());
                newAttendance.setStartTime(new Date());
                attendanceRepository.save(newAttendance); // 출근 정보 저장
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

}
