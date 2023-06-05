package com.strato.skylift.notice.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.approval.dto.ApprovalDto;

import lombok.Data;

@Data
public class RequestDto {
    private Long requestCode;
    private String requestReason;
    private String requsetType;
    private Date requestStart;
    private Date requestEnd;
    @JsonIgnore
    private List<ApprovalDto> approvals;
	@Override
	public String toString() {
		return "RequestDto [requestCode=" + requestCode + ", requestReason=" + requestReason + ", requsetType="
				+ requsetType + ", requestStart=" + requestStart + ", requestEnd=" + requestEnd + "]";
	}
    
   
}

