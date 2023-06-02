package com.strato.skylift.notice.service;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

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

//1. 공지사항 전체 목록 조회(사용자, 관리자)  - 삭제되지 않은 것만 조회
	public Page<NoticeDto> selectNoticeListDelN(int page) {
		log.info("[NoticeService] selectNoticeListDelN start ============================== ");
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("noticeCode").descending());
		
		Page<Notice> noticeList = noticeRepository.findByNoticeDelYn(pageable, "N");
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));

		/* 클라이언트 측에서 서버에 저장 된 이미지 요청 시 필요한 주소로 가공 */
		noticeDtoList.forEach(notice -> notice.setNoticeImgUrl(IMAGE_URL + notice.getNoticeImgUrl()));
		
		log.info("[NoticeService] productDtoList.getContent() : {}", noticeDtoList.getContent());
		
		return noticeDtoList;
	}
	
//2. 공지사항 삭제된 목록 조회(관리자)
	public Page<NoticeDto> selectNoticeListDelY(int page) {
		log.info("[NoticeService] selectNoticeListDelY start ============================== ");
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("noticeCode").descending());
		
		Page<Notice> noticeList = noticeRepository.findByNoticeDelYn(pageable, "Y");
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));

		log.info("[NoticeService] productDtoList.getContent() : {}", noticeDtoList.getContent());
		
		return noticeDtoList;
	}
	




	//유정
	public Page<Notice> getNoticesByDeptCode(String deptCode, int page) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("noticeCode").descending());
		return noticeRepository.findByDepartmentDeptCode(deptCode, pageable);
	}



	public Notice createNotice(NoticeDto noticeDto) {
		Notice notice = modelMapper.map(noticeDto, Notice.class);
		return noticeRepository.save(notice);
	}

/* D. 관리자 공지 등록 */
	@Transactional
	public void insertNotice(NoticeDto noticeDto) {
		log.info("[NoticeService] : noticeDto : {} " , noticeDto);
		
		if(noticeDto.getNoticeImage() != null) {
			String imageName = UUID.randomUUID().toString().replace("-", "");
			NoticeFileDto fileDto = new NoticeFileDto();
			try {
				String replaceFilename = NoticeFileUploadUtils.saveFile(IMAGE_DIR + "/notice", imageName, noticeDto.getNoticeImage());
				fileDto.setFileName(imageName);
				fileDto.setFilePath(replaceFilename);
				fileDto.setFileType("공지사항 첨부이미지");
				Notice newNotice = noticeRepository.save(modelMapper.map(noticeDto, Notice.class));
				fileDto.setNoticeCode(newNotice.getNoticeCode());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Notice newNotice = noticeRepository.save(modelMapper.map(noticeDto, Notice.class));
			
			fileDto.setNoticeCode(newNotice.getNoticeCode());
				
			noticeFileRepository.save(modelMapper.map(fileDto, NoticeFile.class));
			
		} else {
			
			Notice newNotice = noticeRepository.save(modelMapper.map(noticeDto, Notice.class));
		}
	
	}

	public NoticeDto selectNoticeDetail(Long noticeCode) {
		log.info("[NoticeService] selectNoticeDetail start ============================== ");
		log.info("[NoticeService] noticeCode : {}", noticeCode);
		
		Notice notice = noticeRepository.findById(noticeCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 게시글이 없습니다. noticeCode : " + noticeCode));
		log.info("[NoticeService] notice : {}", notice);
		
		NoticeDto noticeDto = modelMapper.map(notice, NoticeDto.class);
		log.info("[NoticeService] noticeDto : {}", noticeDto);
		
		log.info("[NoticeService] selectNoticeDetail end ============================== ");
		return noticeDto;	
	}

/* 검색 - 제목 */
	public Page<NoticeDto> selectNoticeListByNoticeTitle(int page, String noticeTitle) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("noticeCode").ascending());
		
		Page<Notice> noticeList = noticeRepository.findByNoticeTitleContainsAndNoticeDelYn(pageable, noticeTitle, "N");
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
		
		
		log.info("noticeList : {}", noticeList);
		return noticeDtoList;
	}
/* 검색 - 내용 */
	public Page<NoticeDto> selectNoticeListByNoticeContent(int page, String noticeContent) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("noticeCode").ascending());
		
		Page<Notice> noticeList = noticeRepository.findByNoticeTitleContainsAndNoticeDelYn(pageable, noticeContent, "N");
		Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
		
		log.info("noticeList : {}", noticeList);
		return noticeDtoList;
	}


/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	

}
