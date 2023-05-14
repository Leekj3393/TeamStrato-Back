package com.strato.skylift.notice.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
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
	
/* 1. 공지사항 전체 목록 조회(사용자) - 완성 */
	@GetMapping
	public ResponseEntity<ResponseDto> selectNoticeList(@RequestParam(name="page", defaultValue="1") int page){
		
		log.info("[NoticeController] : selectNoticeList start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeList(page);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
	
		log.info("[NoticeController] : selectNoticeList end ==================================== ");
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
	}
	
	
/* 2. 공지사항 부서별 목록 조회 - 미완성 
 * 부서테이블 레포지터리가 생기면 그때 마무리하기!!*/
	@GetMapping("/part/{deptCode}")
	public ResponseEntity<ResponseDto> selectNoticeListByDepartment(
			@RequestParam(name="page", defaultValue="1") int page, @PathVariable Long deptCode){
		
		log.info("[NoticeController] : selectNoticeListByDepartment start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListByDepartment(page, deptCode);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
		log.info("[NoticeController] : selectNoticeListByDepartment end ==================================== ");
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
	} 
	
	/* 3. 공지사항 상태별 목록 조회 - 포스트맨 테스트는 완료!! 
	 * ??*/
	@GetMapping("/status/{noticeStatus}")
	public ResponseEntity<ResponseDto> selectNoticeListByNoticeStatus(
			@RequestParam(name="page", defaultValue="1") int page, @PathVariable String noticeStatus){
		
		log.info("[NoticeController] : selectNoticeListByDepartment start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListByNoticeStatus(page, noticeStatus);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
		log.info("[NoticeController] : selectNoticeListByDepartment end ==================================== ");
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
	} 
	
	/* A.관리자 공지사항 전체 조회 - 포스트맨 테스트 완료!!!! 
	 * 굳이 따로 나눌 필요가 있나 싶다,,,,, 음,,,,, 걍 사용자랑 똑같음
	 * 리액트 isAdmin 요거 사용하는게 좋을 것 같음 근데 아무것도 모르겠음,,, 졸리기 때문에 그냥 자고 내일 하기로,, 일단 보류*/
	@GetMapping("/manage")
	public ResponseEntity<ResponseDto> selectNoticeListForAdmin(@RequestParam(name="page", defaultValue="1") int page){
		
		log.info("[NoticeController] : selectNoticeListForAdmin start ==================================== ");
		log.info("[NoticeController] : page : {}", page);
		
		Page<NoticeDto> noticeDtoList = noticeService.selectNoticeListForAdmin(page);
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);
		
		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(noticeDtoList.getContent());
		
		
		log.info("[NoticeController] : selectNoticeListForAdmin end ==================================== ");
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
	}
	
	
/* B. 관리자 부서별 조회 */
/* C. 관리자 상태별 조회 */
/* D. 관리자 공지 등록 */
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */
/*  */
	
}
