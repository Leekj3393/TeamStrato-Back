package com.strato.skylift.education.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EducationDto {
	
	private Long edCode;

	private String edName;

	private Long edTime;

	private String edStatus;

	private String edType;
	
	@JsonIgnore
	private MultipartFile educationVideos;
}
