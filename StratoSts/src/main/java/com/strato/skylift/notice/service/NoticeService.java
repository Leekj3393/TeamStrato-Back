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

// 2. 부서별 공지사항 목록 조회 - 미완성(부서 레포지터리 생기면 그때 완성할 수 있을 것 같음)
	public Page<NoticeDto> selectNoticeListByDepartment(int page, Long deptCode) {
		log.info("[NoticeService] selectNoticeListByDepartment start ============================== ");
		
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("deptCode").descending());
		
		/* 전달할 부서 엔티티를 먼저 조회한다. */
//		Department findDepartment = departmentRepository.findById(deptCode)
//				.orElseThrow(() -> new IllegalArgumentException("해당 부서가 없습니다. deptCode = "+ deptCode));
//		
//		Page<Notice> noticeList = noticeRepository.findByDepartment(pageable, findDepartment);
//		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
//	
//		
//		log.info("[NoticeService] noticeDtoList.getContent() : {}", noticeDtoList.getContent());
//		
//		log.info("[NoticeService] selectNoticeListByDepartment end ============================== ");
		
		
		return null;
	}
	

}
