package com.strato.skylift.approval.dto;

import java.util.Date;

import com.strato.skylift.member.dto.MbMemberDto;

import lombok.Data;

@Data
public class ApprovalLineDto {

private Long appLineCode;
	
	private ApprovalDto approval;
	
	private String appStatus;
	
	private String appPriorYn;

	private Date appTime;
	
	private MbMemberDto member;
	
}
