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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/skylift/myPage")
public class MyPageController {

	private final MyPageService myPageService;
	private final AttendanceRepository attendanceRepository;
	private final MyPageRepository myPageRepository;
	private final NoticeService noticeService;

	// 전체 사원수
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

	// 해당하는 멤버의 근태조회하기
	@GetMapping("/members/{memberCode}/attendance")
	public ResponseEntity<ResponseDto> getAttendanceByMemberCode(@PathVariable("memberCode") Long memberCode) {
		List<MbAttendanceDto> attendanceList = myPageService.getAttendanceByMemberCode(memberCode);
		ResponseDto responseDto = new ResponseDto(HttpStatus.OK, "조회 성공", attendanceList);
		return ResponseEntity.ok().body(responseDto);
	}

	// 전체 멤버 조회하기
	@GetMapping("/members")
	public ResponseEntity<ResponseDto> selectMemberList(@RequestParam(name = "page", defaultValue = "1") int page,
			@AuthenticationPrincipal MbMemberDto mbMemberDto) {
		log.info("[MyPageController1] : selectMemberList start=========");
		Page<MbMemberDto> memberDtoList = myPageService.selectMemberList(page);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(memberDtoList);

		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(memberDtoList.getContent());

		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", responseDtoWithPaging));
	}

	// 로그인한 멤버의 정보 조회하기
//        @GetMapping("/members/{memberCode}")
//    public ResponseEntity<ResponseDto> selectMemberCode(@PathVariable("memberCode") Long memberCode){
//            return  ResponseEntity
//                    .ok()
//                    .body(new ResponseDto(HttpStatus.OK,"조회성공",myPageService.findByMemberCode(memberCode)));
//
//        }

	@GetMapping("/members/{memberCode}")
	public ResponseEntity<ResponseDto> selectMemberCode(@PathVariable("memberCode") Long memberCode,
			@AuthenticationPrincipal MbMemberDto mbMemberDto // 현재 인증된 사용자의 정보 주입
	) {
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "조회성공", myPageService.findByMemberCode(memberCode)));
	}

	// 어덴티 케이션 멤버조회
	@GetMapping("/membersInfo")
	public ResponseEntity<ResponseDto> selectMemberCodeAutenti(@AuthenticationPrincipal MbMemberDto member) // 현재 인증된
																											// 사용자의 정보
																											// 주입)
	{
		log.info("memberController: {}", member);
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "조회성공", myPageService.selectMyInfo(member.getMemberCode())));
	}

	// 멤버 기본정보 수정하기
	@PutMapping("/members/modify/{memberCode}")
	public ResponseEntity<ResponseDto> updateMember(@ModelAttribute MbMemberDto mbMemberDto) {
		myPageService.updateMember(mbMemberDto);

		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "회원 정보 변경 완료"));
	}

	// 근태관리 - 출석 누르기
	@PostMapping("/attendance")
	public ResponseEntity<?> handleAttendance(@AuthenticationPrincipal MbMemberDto member) {

		myPageService.manageAttendance(member.getMemberCode());
		return ResponseEntity.ok().build();
	}

	// 퇴근 시간 업데이트
	@PostMapping("/attendance/endTime")
	public ResponseEntity<?> handleAttendanceUpdate(@AuthenticationPrincipal MbMemberDto member) {

		try {
			myPageService.manageAttendanceEndChange(member.getMemberCode());
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 외출시간 출근시간은 그대로 두고, 만약 퇴근이 되어있으면 "퇴근하셔서 외출이 안됩니다"라고 뜨게하기
	@PostMapping("/attendance/outTime")
	public ResponseEntity<?> handleAttendanceOutUpdate(@AuthenticationPrincipal MbMemberDto member) {
		try {
			myPageService.manageAttendanceEndChange(member.getMemberCode());
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 출근이랑 외출이 둘다 날짜가 찏혔을때만 복귀가 가능하게 하고싶어
	@PostMapping("/attendance/returnTime/{memberCode}")
	public ResponseEntity<?> handleAttendanceReturnUpdate(@AuthenticationPrincipal MbMemberDto member) {
		try {
			myPageService.manageAttendanceEndChange(member.getMemberCode());
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 공지사항 전체 조회

	private boolean isToday(Date attendanceDate) {
		Calendar cal1 = Calendar.getInstance(); // 현재 날짜와 시간
		Calendar cal2 = Calendar.getInstance(); // 주어진 attendanceDate의 날짜와 시간

		cal1.setTime(new Date()); // 현재 날짜와 시간 설정
		cal2.setTime(attendanceDate); // 주어진 attendanceDate의 날짜와 시간 설정

		// 년, 월, 일이 모두 동일한지 확인
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
	}

}
