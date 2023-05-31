package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_SALARY_STATEMENT")
@SequenceGenerator(name = "SALARY_SEQ_GENERATOR"
                  , sequenceName = "SEQ_SALARY_STATEMENT"
                  , initialValue = 1 , allocationSize = 0)
@DynamicInsert @Getter @Setter
public class SalaryStatement
{
    @Id @Column(name = "SALARY_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SALARY_SEQ_GENERATOR")
    private Long salaryCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @Column(name = "SALARY_CLASSIFICATION")
    private String salaryClassification;

    @Column(name = "SALARY")
    private Long salary;

    @Column(name = "ALLOWANCE")
    private Long allowance;

    @Column(name = "INCOME_TAX")
    private Long incomeTax;

    @Column(name = "EMPLOYMENT_INSURANCE")
    private Long employmentInsurance;

    @Column(name = "NATIONAL_PENSION")
    private Long nationalPesion;

    @Column(name = "MEDICAL_INSURANCE")
    private Long medicalInsurance;

    @Column(name = "TOTAL_AMOUNT")
    private Long totalAmount;

    @Column(name = "TOTAL_DEDUCTED")
    private Long totalDeducted;

    @Column(name = "PAYMENT_AMOUNT")
    private Long paymentAmount;

    @Column(name = "SALALE_DATE")
    private Date salaleDate;

}
