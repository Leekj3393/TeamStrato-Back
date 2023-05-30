package com.strato.skylift.member.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MbFileDto {
	
	private Long fileCode;

	private String fileName;

	private String filePath;

	private String fileType;

	private Long memberCode;
	
	@JsonIgnore
	private MultipartFile educationImage;
	
	@JsonIgnore
	private String fileTitle;
}
