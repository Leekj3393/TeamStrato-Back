package com.strato.skylift.notice.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.Data;

@Data
public class NoticeDto {
	
	private Long noticeCode;
	private String noticeTitle;
	private String noticeContent;
	private String noticeType;
	private String noticeDelYn;
	private String noticeStatus;
	private Date noticeRegistDate;
	private Date noticeInitDate;
	private Date noticeEndDate;
	private MbMemberDto member;
	private MbDepartmentDto department;
	
	@JsonIgnore
	private MultipartFile noticeImage;
	

/*
NOTICE_CODE	NUMBER
NOTICE_TITLE	VARCHAR2(2000 BYTE)
NOTICE_CONTENT	VARCHAR2(3500 BYTE)
NOTICE_TYPE	VARCHAR2(1000 BYTE)
NOTICE_DEL_YN	VARCHAR2(1000 BYTE)
NOTICE_STATUS	VARCHAR2(1000 BYTE)
NOTICE_REGIST_DATE	DATE
NOTICE_INIT_DATE	DATE
NOTICE_END_DATE	DATE
MEMBER_CODE	NUMBER
DEPT_CODE	VARCHAR2(1000 BYTE)*/
	
}
