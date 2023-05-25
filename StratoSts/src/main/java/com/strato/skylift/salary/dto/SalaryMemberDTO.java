package com.strato.skylift.salary.dto;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.File;
import com.strato.skylift.entity.Job;
import com.strato.skylift.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SalaryMemberDTO
{
    private Long memberCode;
    private String memberId;
    private String memberPwd;
    private String memberName;
    private String residentNo;
    private String gender;
    private String phone;
    private String address;
    private String bankName;
    private String bankNo;
    private Long memberSalary;
    private Job job;
    private Department department;
    private MemberRole memberRole;
}
