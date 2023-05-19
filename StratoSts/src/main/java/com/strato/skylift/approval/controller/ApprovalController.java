package com.strato.skylift.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.service.ApprovalService;
import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/skylift/approval")
public class ApprovalController {
	
	private final ApprovalService appServ;
	
	public ApprovalController (ApprovalService appServ) {
		
		this.appServ = appServ;
		
	}
	
/* 1. 결재문서 조회 - 결재 대기함 */
/* 2. 결재문서 조회 - 결재 진행함 */
/* 3. 결재문서 조회 - 결재 완료함 */
/* 4. 결재문서 조회 - 결재 반려함 */
/* 5. 메인화면 결재 대기문서 조회 */
/* 6. 기안문 작성 - 진행중 */
	//로그인한 직원의 정보 조회
//	@GetMapping("/memberInfo")
//	public ResponseEntity<MbMemberDto> getMemberInfo(@AuthenticationPrincipal MbMemberDto memberDto, Long memberCode) {
////		memberDto = new MbMemberDto();
////		memberDto.setMemberCode(1L);
//	    memberCode = memberDto.getMemberCode();
//	    MbMemberDto memberInfo = appServ.selectMemberDetailForApproval(memberCode);
//	    return ResponseEntity.ok(memberInfo);
//	}
	
	@GetMapping("/memberInfo")
	public ResponseEntity<ResponseDto> getMemberInfoForApproval(@AuthenticationPrincipal MbMemberDto memberDto) {
		
//		memberDto = new MbMemberDto();
//		memberDto.setMemberCode(1L);
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "기안자 조회 성공", appServ.getMemberInfoForApproval(memberDto.getMemberCode())));
	}
	
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> registApproval(@RequestBody ApprovalDto appDto,
													  @AuthenticationPrincipal MbMemberDto memberDto
														  ) {
//		memberDto = new MbMemberDto();
//		memberDto.setMemberCode(1L);
		
		appDto.setMemberDto(memberDto);
		log.info("memberDto : {}", memberDto);
		appServ.registApp(appDto);
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재 등록 성공"));
	}
	
	
/* 7. 결재선, 열람인 선정
 * 	- 기안문 작성 -> 전자결재 페이지에서 함
 * 	- 휴가, 휴직, 퇴직 신청 -> 마이페이지에서 한 뒤 넘어옴
 * 	- 장비 구매, 장비 수리, 장비 폐기 신청 -> 장비관리 페이지에서 한 뒤 넘어옴  */
	@PostMapping("/appline")
	public ResponseEntity<ResponseDto> insertAppLine (@RequestBody ApprovalLineDto applineDto, @AuthenticationPrincipal MbMemberDto memberDto){
		appServ.insertAppLine(applineDto);
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "결재선 등록 성공"));
	}
	
	// 직원 전체 목록 조회
	@GetMapping("/memberList")
	public ResponseEntity<ResponseDto> selectMemberList(@AuthenticationPrincipal MbMemberDto memberDto) {

//	    List<MbDepartmentDto> deptListDto = appServ.selectDeptList();
//	    List<MbJobDto> jobListDto = appServ.selectJobList();
	    List<MbMemberDto> memberListDto = appServ.selectMemberList();
	    
	    
	    
//	    log.info("deptListDto : {}", deptListDto);
//	    log.info("jobListDto : {}", jobListDto);
	    log.info("memberListDto : {}", memberListDto);
	    
	    Map<String, Object> data = new HashMap<>();
//	    data.put("dept", deptListDto);
//	    data.put("job", jobListDto);
	    data.put("appline", memberListDto);

	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 전체 목록 조회 완료", data));
	}
	
	
	
/* 8. 결재 승인  */
/* 9. 결재 반려  */
/*  */
/*  */
/*  */

}
