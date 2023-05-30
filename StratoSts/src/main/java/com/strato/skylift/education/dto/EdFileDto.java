package com.strato.skylift.education.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdFileDto {
	
	private Long fileCode;

	private String fileName;

	private String filePath;

	private String fileType;
	
	private Long edCode;
	
}
