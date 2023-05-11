package com.strato.skylift.member.dto;

import com.strato.skylift.education.dto.ClassDto;
import com.strato.skylift.education.dto.EducationDto;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.Data;

@Data
public class MbFileDto {
	
	private Long fileCode;

	private String fileName;

	private String filePath;

	private String fileType;

	private MbMemberDto member;

	private EducationDto education;

	private ClassDto edClass;
}
