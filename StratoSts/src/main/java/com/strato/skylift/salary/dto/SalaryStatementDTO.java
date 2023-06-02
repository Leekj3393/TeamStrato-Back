package com.strato.skylift.salary.dto;

import com.strato.skylift.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
@AllArgsConstructor
@Getter @Setter @ToString
public class SalaryStatementDTO
{
    private Long memberCode;
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
    private Date salaleDate;
}
