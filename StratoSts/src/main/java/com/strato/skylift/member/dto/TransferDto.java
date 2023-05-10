package com.strato.skylift.member.dto;

import java.util.Date;

import com.strato.skylift.department.dto.DepartmentDto;
import com.strato.skylift.job.dto.JobDto;

import lombok.Data;

@Data
public class TransferDto {

	private Long transferCode;
	
	private Date transferDate; 
	
	private MemberDto member;
	
	private JobDto job;
	
	private DepartmentDto department;
	
}
