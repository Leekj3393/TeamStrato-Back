package com.strato.skylift.approval.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.service.ApprovalService;
import com.strato.skylift.common.ResponseDto;

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
/* 6. 기안문 작성  */
	@PostMapping
	public ResponseEntity<ResponseDto> registApproval(){
		return null;
	}
/* 7. 결재선, 열람인 선정
 * 	- 기안문 작성 -> 전자결재 페이지에서 함
 * 	- 휴가, 휴직, 퇴직 신청 -> 마이페이지에서 한 뒤 넘어옴
 * 	- 장비 구매, 장비 수리, 장비 폐기 신청 -> 장비관리 페이지에서 한 뒤 넘어옴  */
	@GetMapping("/appline/department")
	public  ResponseEntity<ResponseDto> getDeptList (){
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "부서 조회를 성공했습니다.", appServ.getDeptList()));
	}
	
	@GetMapping("/appline/members")
	public ResponseEntity<ResponseDto> getMemberList (){
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 조회를 성공했습니다.", appServ.getMemberList()));
	}
	@PostMapping("/appline")
	public ResponseEntity<ResponseDto> insertAppLine (@ModelAttribute ApprovalLineDto applineDto){
		
		appServ.insertAppLine(applineDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "결재선 등록 성공"));
	}
	
	
/* 8. 결재 승인  */
/* 9. 결재 반려  */
/*  */
/*  */
/*  */

}
