package com.strato.skylift.salary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class SalaryDTO
{
    private Long salaryCode;
    private MbMemberDto member;
    private String salaryClassification;
    private Long salary;
    private Long allowance;
    private Long incomeTax;
    private Long employmentInsurance;
    private Long nationalPesion;
    private Long medicalInsurance;
    private Long totalAmount;
    private Long totalDeducted;
    private Long paymentAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date salaleDate;
    @JsonFormat(pattern = "yyyy-MM")
    private Date salaryDay;

    private Long totalTime;
    private Long overTime;
    private Long late;
    private Long out;
    private Long earlyLeave;
    private Long absence;
    private List<AttendanceDTO> attendance;
    private PagingButtonInfo pageInfo;
}
