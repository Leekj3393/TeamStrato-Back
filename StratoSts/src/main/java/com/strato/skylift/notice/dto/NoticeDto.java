package com.strato.skylift.notice.dto;

import java.util.Date;
import java.util.List;

import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.Data;

@Data @ToString
public class NoticeDto {
	
	private Long noticeCode;
	private String noticeTitle;
	private String noticeContent; // 추가된 필드
	private String noticeType;
	private String noticeDelYn;
	private Date noticeRegistDate;
	private MbMemberDto member;
	private MbDepartmentDto department;
	private List<NoticeFileDto> noticefiles;
	
	/* DB 컬럼으로 존재하지는 않지만(entity의 필드로 선언하지 않는다) 
	 * 클라이언트에서 넘겨주는 이미지 파일을 저장할 수 있는 필드 선언 */
	@JsonIgnore
	private MultipartFile noticeImage;
	
	private String noticeImgUrl;
	

/*
NOTICE_CODE	NUMBER
NOTICE_TITLE	VARCHAR2(2000 BYTE)
NOTICE_CONTENT	VARCHAR2(3500 BYTE)
NOTICE_TYPE	VARCHAR2(1000 BYTE)
NOTICE_DEL_YN	VARCHAR2(1000 BYTE)
NOTICE_REGIST_DATE	DATE
MEMBER_CODE	NUMBER
DEPT_CODE	VARCHAR2(1000 BYTE)*/
	
}
