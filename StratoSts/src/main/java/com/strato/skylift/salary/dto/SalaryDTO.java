package com.strato.skylift.salary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.strato.skylift.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class SalaryDTO
{
    private Long salaryCode;
    private Member member;
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
}
