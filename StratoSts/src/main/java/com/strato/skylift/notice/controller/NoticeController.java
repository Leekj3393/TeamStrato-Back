package com.strato.skylift.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("skylift/notice")
public class NoticeController {

	private final NoticeService noticeService;
	
	public NoticeController (NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
/* 1. 공지사항 전체 목록 조회(사용자, 관리자) -삭제여부가 'N'인 것만  */
	@GetMapping
	public ResponseEntity<ResponseDto> selectNoticeListDelN(@AuthenticationPrincipal MbMemberDto memberDto, @RequestParam(name="page", defaultValue="1") int page){
		
		log.info("[NoticeController] : selectNoticeList start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListDelN(page);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
	
		log.info("[NoticeController] : selectNoticeList end ==================================== ");
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
	}
	
/* 검색 - 제목 */
/*  공지사항 전체 목록 조회(사용자, 관리자) - 제목 기준/삭제상태 'n' */
	@GetMapping("/search/title/{noticeTitle}")
	public ResponseEntity<ResponseDto> selectNoticeListByNoticeTitle (
		@RequestParam(name="page", defaultValue="1") int page, @RequestParam(name="search") String noticeTitle) {
		log.info("[NoticeController] : selectNoticeListByNoticeTitle start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		log.info("[NoticeController] : noticeTitle : {}", noticeTitle);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListByNoticeTitle(page, noticeTitle);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[NoticeController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
		log.info("[NoticeController] : selectNoticeListByNoticeTitle end ==================================== ");
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "제목 기준 공지사항 조회성공", responseDtoWithPaging));
	}
	
/* 검색 - 내용 */
/*  공지사항 전체 목록 조회(사용자, 관리자) - 내용 기준  */
	@GetMapping("/search/content/{noticeContent}")
	public ResponseEntity<ResponseDto> selectNoticeListByNoticeContent (
			@RequestParam(name="page", defaultValue="1") int page, @RequestParam(name="search") String noticeContent) {
		
		log.info("[NoticeController] : selectNoticeListByNoticeContent start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		log.info("[NoticeController] : noticeContent : {}", noticeContent);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListByNoticeContent(page, noticeContent);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[NoticeController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
		log.info("[NoticeController] : selectNoticeListByNoticeTitle end ==================================== ");
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "내용 기준 공지사항 조회성공", responseDtoWithPaging));
	}
	
// 관리자- 삭제된 공지사항 목록조회 - postman 테스트완료
	@GetMapping("/deleted")
	public ResponseEntity<ResponseDto> selectNoticeListDelY(@AuthenticationPrincipal MbMemberDto memberDto, @RequestParam(name="page", defaultValue="1") int page){

		log.info("[NoticeController] : selectNoticeListDelY start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListDelY(page);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
	
		log.info("[NoticeController] : selectNoticeListDelY end ==================================== ");
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "삭제된 공지 조회 성공", responseDtoWithPaging));
	}
	

/* 공지사항 게시글 조회 - 첨부파일 제외 테스트 완료 */
	@GetMapping("/detail/{noticeCode}")
	public ResponseEntity<ResponseDto> selectNoticeDetail(@PathVariable Long noticeCode) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "게시글 상세페이지 조회 성공", noticeService.selectNoticeDetail(noticeCode)));
	}

	
	
/* D. 관리자 공지 등록 */
	@PostMapping("/regist")
	public ResponseEntity<ResponseDto> insertNotice(@ModelAttribute NoticeDto noticeDto, 
											@AuthenticationPrincipal MbMemberDto memberDto) {
		log.info("noticeDto : {}" + noticeDto);
		log.info("member : {}" + memberDto);

		noticeDto.setMember(memberDto);
		
		noticeService.insertNotice(noticeDto);
		
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "공지사항 등록 성공"));
	}
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */
/*  */
	
}
