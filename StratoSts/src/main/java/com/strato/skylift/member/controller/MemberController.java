package com.strato.skylift.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.member.dto.MemberDto;
import com.strato.skylift.member.service.MemberService;

public class MemberController {

	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	/* 직원 전체 조회 */
	@GetMapping("/members")
	public ResponseEntity<ResponseDto> selectMemberList(@RequestParam(name="page", defaultValue="1") int page) {
		
		Page<MemberDto> memberDtoList = memberService.selectMemberList(page);
		
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
	
	/* 직원 등록 */
	@PostMapping("/members")
	public ResponseEntity<ResponseDto> insertMember(@ModelAttribute MemberDto memberDto) {
		
		memberService.insertMember(memberDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "직원 등록 성공"));
	}
	
	/* 직원 수정 */
}
