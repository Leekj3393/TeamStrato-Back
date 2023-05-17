package com.strato.skylift.approval.controller;

import com.strato.skylift.notice.dto.RequestDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.repository.AppMemberRepository;
import com.strato.skylift.approval.service.ApprovalService;
import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;
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
/* 5. 결재문서 조회 - 상신 문서함(본인이 상신한 문서함) */
/* 6. 기안문 작성 -완료?? */
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> registApproval(@RequestBody ApprovalDto appDto,
													  @AuthenticationPrincipal MbMemberDto memberDto
														  ) {
		memberDto = new MbMemberDto();
		memberDto.setMemberCode(1L);
		
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
	public ResponseEntity<ResponseDto> insertAppLine (@RequestBody ApprovalLineDto applineDto){
		appServ.insertAppLine(applineDto);
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "결재선 등록 성공"));
	}
	// 직원 전체 목록 조회
	@GetMapping("/memberList")
	public ResponseEntity<ResponseDto> selectMemberList() {

	    List<MbMemberDto> memberListDto = appServ.selectMemberList();
	    List<MbDepartmentDto> deptListDto = appServ.selectDeptList();
	    
	    log.info("jobListDto : {}", memberListDto);
	    log.info("deptListDto : {}", deptListDto);
	    
	    Map<String, Object> data = new HashMap<>();
	    data.put("job", memberListDto);
	    data.put("dept", deptListDto);

	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 전체 목록 조회 완료", data));
	}
	
	
	
/* 8. 결재 승인  */
/* 9. 결재 반려  */
/*  */
/*  */
/*  */

}
