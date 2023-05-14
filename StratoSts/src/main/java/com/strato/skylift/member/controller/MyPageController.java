package com.strato.skylift.member.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbAttendanceDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.AttendanceRepository;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.member.service.MyPageService;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/skylift/myPage")
public class MyPageController {

        private final MyPageService myPageService;
        private final AttendanceRepository attendanceRepository;
        private final MyPageRepository myPageRepository;
        private final NoticeService noticeService;

        //전체 사원수
    @GetMapping("/membersAll")
    public ResponseEntity<ResponseDto> getTotalMemberCount() {
        log.info("[MyPageController1] : getTotalMemberCount start=========");

        // 전체 사원 수를 조회하는 쿼리 실행
        long totalMemberCount = myPageService.getTotalMemberCount();
        log.info("전체 사원 수: {}", totalMemberCount);

        // 숫자로만 표시하는 응답 데이터 생성
        Map<String, Long> data = new HashMap<>();
        data.put("totalMemberCount", totalMemberCount);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", data));
    }





    //전체 멤버 조회하기
        @GetMapping("/members")
        public ResponseEntity<ResponseDto> selectMemberList(@RequestParam(name="page", defaultValue="1") int page){
            log.info("[MyPageController1] : selectMemberList start=========");
            Page<MbMemberDto> memberDtoList = myPageService.selectMemberList(page);
            PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(memberDtoList);

            ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
            responseDtoWithPaging.setPageInfo(pageInfo);
            responseDtoWithPaging.setData(memberDtoList.getContent());

            return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"조회성공",responseDtoWithPaging));
        }

        //로그인한 멤버의 정보 조회하기
        @GetMapping("/members/{memberCode}")
    public ResponseEntity<ResponseDto> selectMemberCode(@PathVariable("memberCode") Long memberCode){
            return  ResponseEntity
                    .ok()
                    .body(new ResponseDto(HttpStatus.OK,"조회성공",myPageService.findByMemberCode(memberCode)));

        }
        //멤버 기본정보 수정하기
        @PutMapping("/members/{memberCode}")
    public ResponseEntity<ResponseDto> updateMember(@ModelAttribute MbMemberDto mbMemberDto) {
            myPageService.updateMember(mbMemberDto);

            return ResponseEntity
                    .ok()
                    .body(new ResponseDto(HttpStatus.OK,"회원 정보 변경 완료"));
        }

        //근태관리 - 출석 누르기
        @PostMapping("/attendance/{memberCode}")
        public ResponseEntity<?> handleAttendance(@PathVariable Long memberCode) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());

            Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

                Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);

                // 출근 정보가 있는지 확인하기
                if (attendanceOptional.isPresent()) {
                    Attendance attendance = attendanceOptional.get();

                    // 이미 출근한 경우
                    if (attendance.getStartTime() != null) {
                        throw new RuntimeException("이미 출근하셨습니다.");
                    }

                    // 출근 버튼이 오늘이면 업데이트 되게하기
                    if (isToday(attendance.getAttendanceDate())) {
                        Date startTime = new Date(); // 출근 시간 업데이트
                        attendance.setStartTime(startTime);
                        attendanceRepository.save(attendance); // 출근 정보 업데이트

                        log.info("출근 요청 - 멤버 코드: {}, 요청 시간: {}", memberCode, currentTime);
                        log.info("출근 시간: {}", dateFormat.format(startTime));
                        log.info("퇴근 시간: {}", attendance.getEndTime());
                        log.info("외출 시간: {}", attendance.getOutTime());
                        log.info("복귀 시간: {}", attendance.getReturnTime());
                    } else {
                        throw new RuntimeException("출근은 하루에 한 번만 가능합니다.");
                    }
                } else {
                    // 오늘 처음 출근하는 경우, 새로운 Attendance 생성
                    Attendance newAttendance = new Attendance();
                    newAttendance.setMember(member);
                    newAttendance.setStatus("출근");
                    newAttendance.setAttendanceDate(new Date());
                    Date startTime = new Date(); // 출근 시간 설정
                    newAttendance.setStartTime(startTime);
                    attendanceRepository.save(newAttendance);

                    log.info("처음 출근 요청 - 멤버 코드: {}, 요청 시간: {}", memberCode, currentTime);
                    log.info("출근 시간: {}", dateFormat.format(startTime));
                }
            }
            return ResponseEntity.ok().build();
        }


    //출근 시간은 그대로 두고 퇴근시간 누르기, 만약 출근 시간이 안눌러져 있으면 '먼저 출근을 하세요!' 뜨게 하기
    @PatchMapping("/attendance/endTime/{memberCode}")
    public ResponseEntity<?> handleAttendanceUpdate(@PathVariable Long memberCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 이미 퇴근한 경우
                if (attendance.getEndTime() != null) {
                    throw new RuntimeException("이미 퇴근하셨으므로 다시 퇴근할 수 없습니다.");
                }

                // 퇴근 시간 업데이트
                Date endTime = new Date();
                attendance.setEndTime(endTime);
                attendance.setStatus("퇴근");
                attendanceRepository.save(attendance); // 출근 정보 업데이트

                log.info("퇴근 요청 - 멤버 코드: {}, 요청 시간: {}", memberCode, currentTime);
                log.info("출근 시간: {}", dateFormat.format(attendance.getStartTime()));
                log.info("퇴근 시간: {}", dateFormat.format(endTime));

                return ResponseEntity.ok().build();
            } else {
                // 출근 정보가 없는 경우 처리
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("출근을 먼저 해주세요.");
            }
        } else {
            // 멤버가 존재하지 않는 경우 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 다시 확인하세요.");
        }
    }




    //외출시간 출근시간은 그대로 두고, 만약 퇴근이 되어있으면 "퇴근하셔서 외출이 안됩니다"라고 뜨게하기
    @PatchMapping("/attendance/outTime/{memberCode}")
    public ResponseEntity<?> handleAttendanceOutUpdate(@PathVariable Long memberCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();
                // 출근 상태인지 체크
                if (attendance.getStatus().equals("출근")) {
                    // 이미 퇴근한 경우
                    if (attendance.getEndTime() != null) {
                        throw new RuntimeException("이미 퇴근하셨으므로 외출이 불가능합니다.");
                    }

                    // 출근 상태에서 외출 버튼을 누를 때 외출 시간 업데이트
                    if (attendance.getOutTime() == null) {
                        Date outTime = new Date(); // 외출 시간 업데이트
                        attendance.setOutTime(outTime);
                        attendance.setStatus("외출"); // 출석 상태를 "외출"으로 변경
                        attendanceRepository.save(attendance); // 출석 정보 업데이트

                        log.info("외출 요청 - 멤버 코드: {}, 요청 시간: {}", memberCode, currentTime);
                        log.info("출근 시간: {}", dateFormat.format(attendance.getStartTime()));
                        log.info("외출 시간: {}", dateFormat.format(outTime));

                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.badRequest().body("이미 외출하셨습니다.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("출근을 먼저 해주세요.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("출근을 먼저 해주세요.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 다시 확인하세요.");
        }
    }


    //출근이랑 외출이 둘다 날짜가 찏혔을때만 복귀가 가능하게 하고싶어
    @PatchMapping("/attendance/returnTime/{memberCode}")
    public ResponseEntity<?> handleAttendanceReturnUpdate(@PathVariable Long memberCode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        Optional<Member> memberOptional = myPageRepository.findByMemberCode(memberCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            Optional<Attendance> attendanceOptional = attendanceRepository.findAllByMemberMemberCode(memberCode);
            if (attendanceOptional.isPresent()) {
                Attendance attendance = attendanceOptional.get();

                // 출근과 외출 상태를 함께 검사
                if (attendance.getStatus().equals("외출")) {
                    // 이미 퇴근한 경우
                    if (attendance.getEndTime() != null) {
                        throw new RuntimeException("이미 퇴근하셨으므로 복귀가 불가능합니다.");
                    } else {
                        Date returnTime = new Date(); // 복귀 시간 업데이트
                        attendance.setReturnTime(returnTime);
                        attendance.setStatus("복귀"); // 출석 상태를 "복귀"로 변경
                        attendanceRepository.save(attendance); // 출석 정보 업데이트

                        log.info("복귀 요청 - 멤버 코드: {}, 요청 시간: {}", memberCode, currentTime);
                        log.info("출근 시간: {}", dateFormat.format(attendance.getStartTime()));
                        log.info("복귀 시간: {}", dateFormat.format(returnTime));

                        return ResponseEntity.ok().build();
                    }
                } else {
                    return ResponseEntity.badRequest().body("외출한 후에만 복귀할 수 있습니다.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 다시 확인하세요.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 다시 확인하세요.");
        }
    }

    //  공지사항 전체 조회








    private boolean isToday(Date attendanceDate) {
        Calendar cal1 = Calendar.getInstance(); // 현재 날짜와 시간
        Calendar cal2 = Calendar.getInstance(); // 주어진 attendanceDate의 날짜와 시간

        cal1.setTime(new Date()); // 현재 날짜와 시간 설정
        cal2.setTime(attendanceDate); // 주어진 attendanceDate의 날짜와 시간 설정

        // 년, 월, 일이 모두 동일한지 확인
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }



}
