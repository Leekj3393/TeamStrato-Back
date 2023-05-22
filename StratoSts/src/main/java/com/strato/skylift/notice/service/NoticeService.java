package com.strato.skylift.notice.service;

import java.io.IOException;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.strato.skylift.entity.Notice;
import com.strato.skylift.entity.NoticeFile;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.dto.NoticeFileDto;
import com.strato.skylift.notice.repository.NoticeFileRepository;
import com.strato.skylift.notice.repository.NoticeRepository;
import com.strato.skylift.notice.util.NoticeFileUploadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	private final NoticeFileRepository noticeFileRepository;
//	private final DepartmentRepository departmentRepository;
	private final ModelMapper modelMapper;
	
	@Value("${image.image-url}")
	private String IMAGE_URL;
	
	@Value("${image.image-dir}")
	private String IMAGE_DIR;
	
	public NoticeService (NoticeRepository noticeRepository,
						  NoticeFileRepository noticeFileRepository,
						  ModelMapper modelMapper /*, DepartmentRepository departmentRepository*/) {
		this.noticeRepository = noticeRepository;
		this.noticeFileRepository = noticeFileRepository;
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
	public void insertNotice(NoticeDto noticeDto) {
		String imageName = UUID.randomUUID().toString().replace("-", "");
		
		NoticeFileDto fileDto = new NoticeFileDto();
		
		try {
//			Member member = 
			
			String replaceFilename = NoticeFileUploadUtils.saveFile(IMAGE_DIR + "/notice", imageName, noticeDto.getNoticeImage());
			
			fileDto.setFileName(imageName);
			fileDto.setFilePath(replaceFilename);
			fileDto.setFileType("공지사항 첨부이미지");
			
			Notice newNotice = noticeRepository.save(modelMapper.map(noticeDto, Notice.class));
			
			fileDto.setNoticeCode(newNotice.getNoticeCode());
				
			noticeFileRepository.save(modelMapper.map(fileDto, NoticeFile.class));
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	}
	
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	

}
