package com.strato.skylift.approval.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ApprovalDto {
	
	private Long appCode;

//  private RequestDto request;
	
	private String appTitle;
	
	private String appContent;
	
	private String appType;
	
	private String appStatus;
	
	private Date appRegistDate;
	
	private Date approvedDate;
	
	private Date appWdlDate;

}
