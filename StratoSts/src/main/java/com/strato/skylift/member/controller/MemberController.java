package com.strato.skylift.member.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.member.dto.MbFileDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/skylift/member")
public class MemberController {

	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
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
	
	/* 직원 등록 - 미완성 */
	@PostMapping("/members")
	public ResponseEntity<ResponseDto> insertMember(@ModelAttribute MbMemberDto memberDto,
			@RequestParam(value="file", required=false) ArrayList<MultipartFile> files
			) {
		
			
			memberService.insertMember(memberDto);
			
		
		
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
	@PutMapping("/members")
	public ResponseEntity<ResponseDto> updateMember(@ModelAttribute MbMemberDto memberDto) {
		
		memberService.updateMember(memberDto);
		
		return ResponseEntity 
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"직원 수정 성공"));
	}
	
}
