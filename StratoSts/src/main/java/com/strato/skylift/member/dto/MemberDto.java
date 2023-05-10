package com.strato.skylift.member.dto;

import java.util.Date;

import com.strato.skylift.department.entity.Department;
import com.strato.skylift.job.entity.Job;

import lombok.Data;

@Data
public class MemberDto {

	private Long memberCode;

	private String memberId;

	private String memberPwd;

	private String memberName;

	private String residentNo;

	private String gender;

	private String phone;

	private String address;

	private String memberStatus;

	private Date memberHireDate;

	private String bankName;

	private String bankNo;

	private Long memberSalary;

	private Long memberAnnual;

	private Job job;

	private Department department;
}
