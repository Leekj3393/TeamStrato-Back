package com.strato.skylift.approval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
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
import com.strato.skylift.approval.repository.AppMemberRepository;
import com.strato.skylift.approval.service.ApprovalService;
import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/skylift/approval")
public class ApprovalController {
	
	private final ApprovalService appServ;
	private final AppMemberRepository appMbRepo;
	private final ModelMapper mm;
	
	public ApprovalController (ApprovalService appServ, AppMemberRepository appMbRepo, ModelMapper mm) {
		
		this.appServ = appServ;
		this.appMbRepo = appMbRepo;
		this.mm = mm;
		
	}
	
/* 1. 결재문서 조회 - 결재 대기함
   2. 결재문서 조회 - 결재 진행함 
   3. 결재문서 조회 - 결재 완료함 
   4. 결재문서 조회 - 결재 반려함
   -포스트맨 테스트  */	
	//결재문서 상태별 조회 --> 로그인한 사람이 상신한 문서들만 조회할 수 있게!! 완료!!
	@GetMapping("/list/{memberCode}/{appStatus}")
	public ResponseEntity<ResponseDto> selectApprovalList(ApprovalDto approval, 
			@RequestParam(name="page", defaultValue="1") int page, 
			@PathVariable("memberCode") Long memberCode,
			@PathVariable("appStatus") String appStatus,
	        @AuthenticationPrincipal MbMemberDto member) {

	    Page<ApprovalDto> approvalDtoList = appServ.selectApprovalList(page, memberCode, appStatus);
	    log.info("[ApprovalController] selectApprovalList Start --------------------------------------------------------------------");
	    log.info("[ApprovalController] approvalDtoList : {}" + approvalDtoList);
	    
	    PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(approvalDtoList);
	    
	    ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
	    responseDtoWithPaging.setPageInfo(pageInfo);
	    responseDtoWithPaging.setData(approvalDtoList.getContent());
	    
	    log.info("[ApprovalController] selectApprovalList End. --------------------------------------------------------------------");
	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재 목록 조회 성공", responseDtoWithPaging));
	}
	
/* 5. 메인화면 결재 요청문서 조회 */
	@GetMapping("/demandList/{memberCode}")
	public ResponseEntity<ResponseDto> getdemandList(ApprovalDto approval, ApprovalLineDto appLine, 
			@RequestParam(name="page", defaultValue="1") int page, 
			@PathVariable("memberCode") Long memberCode,
	        @AuthenticationPrincipal MbMemberDto member) {

	    Page<ApprovalLineDto> appLineDtoList = appServ.getdemandList(page, memberCode);
	    log.info("[ApprovalController] selectApprovalList Start --------------------------------------------------------------------");
	    log.info("[ApprovalController] approvalDtoList : {}" + appLineDtoList);
	    
	    PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(appLineDtoList);
	    
	    ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
	    responseDtoWithPaging.setPageInfo(pageInfo);
	    responseDtoWithPaging.setData(appLineDtoList.getContent());
	    
	    log.info("[ApprovalController] selectApprovalList End. --------------------------------------------------------------------");
	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재 요청 목록 조회 성공", responseDtoWithPaging));
	}
	
	
	
/* 6. 기안문 작성 */
	//로그인한 직원의 정보 조회
	@GetMapping("/memberInfo")
	public ResponseEntity<ResponseDto> getMemberInfoForApproval(@AuthenticationPrincipal MbMemberDto memberDto) {
		//포스트맨 테스트를 위해서 씀!!
//		memberDto = new MbMemberDto();
//		memberDto.setMemberCode(1L);
		return ResponseEntity.ok()
				.body(new ResponseDto(HttpStatus.OK, "작성자 조회 성공", appServ.getMemberInfoForApproval(memberDto.getMemberCode())));
	}
	
	// 기안문 작성하기
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> registApproval(@RequestBody ApprovalDto appDto,
			@AuthenticationPrincipal MbMemberDto memberDto
			) {
		log.info("[ApprovalController] registApproval start ----------------------------------------------------");
		log.info("[ApprovalController] memberDto : {}", memberDto);
		appDto.setMember(memberDto);
		log.info("[ApprovalController] appDto : {}", appDto);
		appServ.registApp(appDto);
		log.info("[ApprovalController] registApproval end ----------------------------------------------------");
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재문서 등록 성공"));
	}
	
	
/* 7. 결재선 선정
 * 	- 기안문 작성 -> 전자결재 페이지에서 함
 * 	- 휴가, 휴직, 퇴직 신청 -> 마이페이지에서 한 뒤 넘어옴
 * 	- 장비 구매, 장비 수리, 장비 폐기 신청 -> 장비관리 페이지에서 한 뒤 넘어옴  */
	// 결재선 저장~~
	@PostMapping("/appline-insert")
	public ResponseEntity<ResponseDto> insertAppLine(@RequestBody ApprovalLineDto appLineDto,
	        @AuthenticationPrincipal MbMemberDto memberDto) {
	    log.info("[ApprovalController] insertAppLine start ----------------------------------------------------");

	    // 서비스 메소드 호출하여 결재선 등록
	    appServ.insertAppLine(appLineDto);
	    log.info("[ApprovalController] insertAppLine end----------------------------------------------------");
	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재선 등록 성공"));
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
	@GetMapping("/{appCode}/appLineInfo")
	public ResponseEntity<ResponseDto> selectAppLineDetail(@PathVariable Long appCode) {
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결재선 정보 조회 성공", appServ.selectAppLineDetail(appCode)));
	}
/*  */
/*  */

}
