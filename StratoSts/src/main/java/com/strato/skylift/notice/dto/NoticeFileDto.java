package com.strato.skylift.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeFileDto {
	
	private Long fileCode;

	private String fileName;

	private String filePath;

	private String fileType;
	
	private Long noticeCode;

}
