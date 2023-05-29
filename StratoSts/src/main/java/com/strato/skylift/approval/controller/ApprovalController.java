package com.strato.skylift.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.service.ApprovalService;
import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
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
	
/* 1. 결재문서 조회 - 결재 대기함
   2. 결재문서 조회 - 결재 진행함 
   3. 결재문서 조회 - 결재 완료함 
   4. 결재문서 조회 - 결재 반려함
   -포스트맨 테스트  */

	// 기안자 정보 조회
//	ㅠㅠㅠㅠㅠ
//	@GetMapping("/writerInfo")
//	public ResponseEntity<ResponseDto> getWriterInfo(ApprovalDto approvalDto, MbMemberDto memberDto) {
//		approvalDto.getAppCode();		
//		
//		
//		return ResponseEntity.ok()
//				.body(new ResponseDto(HttpStatus.OK, "기안자 조회 성공", appServ.getWriterInfo(memberDto.getMemberCode())));
//	}	
	//결재문서 상태별 조회
	@GetMapping("/list/{appStatus}")
	public ResponseEntity<ResponseDto> selectWaitingList(ApprovalDto approval, @RequestParam(name="page", defaultValue="1") int page, @PathVariable String appStatus,
	        @AuthenticationPrincipal MbMemberDto member) {

//		Long memberCode = approval.getMemberDto().getMemberCode();
//		log.info("memberCode : {}" +memberCode);
		
	    Page<ApprovalDto> approvalDtoWList = appServ.selectWaitingList(page, appStatus);
	    log.info("approvalDtoWList : {}" + approvalDtoWList);
	    
	    PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(approvalDtoWList);
	    
	    ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
	    responseDtoWithPaging.setPageInfo(pageInfo);
	    responseDtoWithPaging.setData(approvalDtoWList.getContent());
	    
	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재 대기 목록 조회 성공", responseDtoWithPaging));
	}

	
/* 5. 메인화면 결재 대기문서 조회 */
/* 6. 기안문 작성 - 진행중 */
	//로그인한 직원의 정보 조회
	@GetMapping("/memberInfo")
	public ResponseEntity<ResponseDto> getMemberInfoForApproval(@AuthenticationPrincipal MbMemberDto memberDto) {
		//포스트맨 테스트를 위해서 씀!!
//		memberDto = new MbMemberDto();
//		memberDto.setMemberCode(1L);
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "기안자 조회 성공", appServ.getMemberInfoForApproval(memberDto.getMemberCode())));
	}
	
	// 기안문 작성하기
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> registApproval(@RequestBody ApprovalDto appDto,
			@AuthenticationPrincipal MbMemberDto memberDto
			) {
		
		appDto.setMemberDto(memberDto);
		log.info("memberDto : {}", memberDto);
		appServ.registApp(appDto);
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재문서 등록 성공"));
	}
	
	
/* 7. 결재선 선정
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

	    List<MbDepartmentDto> deptListDto = appServ.selectDeptList();
	    List<MbJobDto> jobListDto = appServ.selectJobList();
	    List<MbMemberDto> memberListDto = appServ.selectMemberList();
	    
	    log.info("deptListDto : {}", deptListDto);
	    log.info("jobListDto : {}", jobListDto);
	    log.info("memberListDto : {}", memberListDto);
	    
	    Map<String, Object> appline = new HashMap<>();
	    appline.put("dept", deptListDto);
	    appline.put("job", jobListDto);
	    appline.put("accessor", memberListDto);

	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 전체 목록 조회 완료", appline));
	}
	
	// 결재선으로 선택된 직원 정보 조회
	@GetMapping("/selectedMember/{memberCode}")
	public ResponseEntity<ResponseDto> selectedMember(MbMemberDto member, @PathVariable Long memberCode) {
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "직원 상세 조회 성공", appServ.getSelectedMemberInfo(member.getMemberCode())));
	}
	
	
	
/* 8. 결재 승인  */
/* 9. 결재 반려  */
	@PutMapping("/approval-accessor")
	public ResponseEntity<ResponseDto> putApprovalAccess(@RequestBody ApprovalLineDto appLineDto,
			  @AuthenticationPrincipal MbMemberDto memberDto
			  ) {
		
		appServ.putApprovalAccess(appLineDto);
		
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "결재 요청 승인/반려 처리 성공"));
	}
	
	
/* 10. 결재 문서 상세페이지 - 포스트맨 테스트 완료!! */
	@GetMapping("/{appCode}")
	public ResponseEntity<ResponseDto> selectApprovalDetail(@PathVariable Long appCode) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재문서 상세페이지 조회 성공", appServ.selectApprovalDetail(appCode)));
	}
/*  */
/*  */

}
