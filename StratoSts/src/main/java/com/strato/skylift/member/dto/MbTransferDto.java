package com.strato.skylift.member.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MbTransferDto {

	private Long transferCode;
	
	private Date transferDate; 
	
	private MbMemberDto member;
	
	private MbJobDto job;
	
	private MbDepartmentDto department;
	
}
