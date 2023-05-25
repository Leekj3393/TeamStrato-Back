package com.strato.skylift.salary.entity;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.File;
import com.strato.skylift.entity.Job;
import com.strato.skylift.entity.MemberRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TBL_MEMBER")
@SequenceGenerator(name="SALARY_MEMBER_SEQ_GENERATOR",
        sequenceName="SEQ_MEMBER",
        initialValue=1, allocationSize=1)
public class SalaryMember
{
    @Id
    @Column(name = "MEMBER_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALARY_MEMBER_SEQ_GENERATOR")
    private Long memberCode;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "MEMBER_PWD")
    private String memberPwd;

    @Column(name = "MEMBER_NAME")
    private String memberName;

    @Column(name = "RESIDENT_NO")
    private String residentNo;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_NO")
    private String bankNo;

    @Column(name = "MEMBER_SALARY")
    private Long memberSalary;

    @ManyToOne
    @JoinColumn(name = "JOB_CODE")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "DEPT_CODE")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "ROLE_CODE")
    private MemberRole memberRole;
}
