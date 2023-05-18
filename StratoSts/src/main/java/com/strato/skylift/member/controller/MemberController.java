package com.strato.skylift.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.dto.MbMemberRoleDto;
import com.strato.skylift.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/skylift/member")
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	public MemberController(MemberService memberService, PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}
	
	/* 직원 전체 조회 */
	@GetMapping("/members")
	public ResponseEntity<ResponseDto> selectMemberList(@RequestParam(name="page", defaultValue="1") int page) {
		
		Page<MbMemberDto> memberDtoList = memberService.selectMemberList(page);
			
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(memberDtoList);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(memberDtoList.getContent());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 조회에 성공했습니다.", responseDtoWithPaging));
		
	}
	
	/* 직원 상세 조회 */
	@GetMapping("/members/{memberCode}")
	public ResponseEntity<ResponseDto> selectMemberDetail(@PathVariable Long memberCode) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 상세조회에 성공했습니다.", memberService.selectMemberDetail(memberCode)));
	}
	
	/* 직원 이미지 조회 */
	@GetMapping("/memberImage/{memberCode}")
	public ResponseEntity<ResponseDto> selectMemberImage(@PathVariable Long memberCode) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 사진조회에 성공했습니다.", memberService.selectMemberImage(memberCode)));
	}
		
	/* 직급, 부서 조회 */
	@GetMapping("/jobDeptList")
	public ResponseEntity<ResponseDto> selectJobDeptList() {

	    List<MbJobDto> jobListDto = memberService.selectJobList();
	    List<MbDepartmentDto> deptListDto = memberService.selectDeptList();
	    
	    log.info("jobListDto : {}", jobListDto);
	    log.info("deptListDto : {}", deptListDto);
	    
	    Map<String, Object> data = new HashMap<>();
	    data.put("job", jobListDto);
	    data.put("dept", deptListDto);

	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직급 및 부서 조회 완료", data));
	}
	
	/* 직원 등록 */
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> insertMember(@ModelAttribute MbMemberDto memberDto) {
		
		System.out.println(memberDto);  
		
		memberService.insertMember(memberDto);
		
		log.info("memberDto : {}", memberDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "직원 등록 성공"));
	}
	
	/* 직원 조회 - 아이디로 검색*/
	@GetMapping("/memberList/searchMbId")
	public ResponseEntity<ResponseDto> selectMemberListByMemberId(
			@RequestParam(name="page", defaultValue="1")int page, @RequestParam(name="search") String memberId ) {
		
		Page<MbMemberDto> memberDtoList = memberService.selectProductListByProductName(page, memberId);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(memberDtoList);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(memberDtoList.getContent());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "아이디 검색 성공", responseDtoWithPaging));
	}
			
	
	/* 직원 조회 - 이름으로 검색*/
	@GetMapping("/memberList/searchMbName")
	public ResponseEntity<ResponseDto> selectMemberListByMemberName(
			@RequestParam(name="page", defaultValue="1")int page, @RequestParam(name="search") String memberName ) {
		
		Page<MbMemberDto> memberDtoList = memberService.selectProductListByProductName(page, memberName);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(memberDtoList);
		   
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(memberDtoList.getContent());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "아이디 검색 성공", responseDtoWithPaging));
	}
	
	/* 직원 수정 */
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateMember(@ModelAttribute MbMemberDto memberDto) {
		
		log.info("memberDto : {}", memberDto);
		
		memberService.updateMember(memberDto);
		
		return ResponseEntity   
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"직원 수정 성공"));
	}
	
	/* 직원 권한 수정 */
	@PutMapping("/updaterole")
	public ResponseEntity<ResponseDto> updateRoleMember(@ModelAttribute MbMemberDto memberDto) {
		
		log.info("memberDto : {}", memberDto);
		
		memberService.updateRoleMember(memberDto);
		
		return ResponseEntity 
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"직원 권한 수정 성공"));
	}
	
	/* 권한 조회 */
	@GetMapping("/memberRoleList")
	public ResponseEntity<ResponseDto> selectMemberRoleList() {

	    List<MbMemberRoleDto> memberRoleDto = memberService.selectMemberRoleList();
	    
	    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직급 및 부서 조회 완료", memberRoleDto));
	}
	
	/* 직원 인사이동 */
	@PutMapping("/updateRequest")
	public ResponseEntity<ResponseDto> updateJobDeptMember(@ModelAttribute MbMemberDto memberDto) {
		
		log.info("memberDto : {}", memberDto);
		
		memberService.updateJobDeptMember(memberDto);
		
		return ResponseEntity 
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"직원 인사이동 성공"));
	}
}
