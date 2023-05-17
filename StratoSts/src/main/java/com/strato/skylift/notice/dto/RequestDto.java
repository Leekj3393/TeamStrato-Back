package com.strato.skylift.notice.dto;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class RequestDto {
    private Long requestCode;
    private String requestReason;
    private String requsetType;
    private Date requestStart;
    private Date requestEnd;
    private List<ApprovalDto> approvals;
}

