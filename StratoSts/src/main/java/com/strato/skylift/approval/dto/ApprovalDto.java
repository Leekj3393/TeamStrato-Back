package com.strato.skylift.approval.dto;

import java.util.Date;

import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.notice.dto.RequestDto;

import lombok.Data;

@Data
public class ApprovalDto {
	
	private Long appCode;
	
	private MbMemberDto member;
	
	private RequestDto request;
	
	private String appTitle;
	
	private String appContent;
	
	private String appType;
	
	private String appStatus;
	
	private Date appRegistDate;
	
	private Date approvedDate;
	
	private Date appWdlDate;

	@Override
	public String toString() {
		return "ApprovalDto [appCode=" + appCode + ", appTitle=" + appTitle + ", appContent=" + appContent
				+ ", appType=" + appType + ", appStatus=" + appStatus + ", appRegistDate=" + appRegistDate
				+ ", approvedDate=" + approvedDate + ", appWdlDate=" + appWdlDate + "]";
	}


	
}
