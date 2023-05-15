package com.strato.skylift.education.dto;

import java.util.Date;

import com.strato.skylift.member.dto.MbMemberDto;

import lombok.Data;

@Data
public class ClassDto {
	
	private Long classCode;

	private Long classTime;

	private String classStatus;

	private Date classDate;

	private Date classEnd;

	private MbMemberDto member;

	private EducationDto education;
}
