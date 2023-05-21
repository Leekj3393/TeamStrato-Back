package com.strato.skylift.notice.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Notice;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.repository.NoticeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
//	private final DepartmentRepository departmentRepository;
	private final ModelMapper modelMapper;
	
	public NoticeService (NoticeRepository noticeRepository, ModelMapper modelMapper /*, DepartmentRepository departmentRepository*/) {
		this.noticeRepository = noticeRepository;
		this.modelMapper = modelMapper;
//		this.departmentRepository = departmentRepository;
	}

//1. 공지사항 전체 목록 조회  - 완료!
	public Page<NoticeDto> selectNoticeList(int page) {
		log.info("[NoticeService] selectNoticeList start ============================== ");
		
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("noticeCode").descending());
		
		Page<Notice> noticeList = noticeRepository.findAll(pageable);
		
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
		
		
		log.info("[NoticeService] productDtoList.getContent() : {}", noticeDtoList.getContent());
		
		return noticeDtoList;
	}

	
/* 검색 - 제목 */
/* 검색 - 내용 */

/* A.관리자 공지사항 전체 조회 */
	public Page<NoticeDto> selectNoticeListForAdmin(int page) {
		
		log.info("[NoticeService] selectNoticeListForAdmin start ============================== ");
		
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("noticeCode").descending());
		
		Page<Notice> noticeList = noticeRepository.findAll(pageable);
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
		

		
		log.info("[NoticeService] noticeDtoList.getContent() : {}", noticeDtoList.getContent());
		
		log.info("[ProductService] selectProductListForAdmin end ============================== ");
		
		return noticeDtoList;
	}
	
/* D. 관리자 공지 등록 */
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	

}
