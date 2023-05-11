package com.strato.skylift.member.dto;

import java.util.Date;

import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;

import lombok.Data;

@Data
public class MbMemberDto {

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

	private MbJobDto job;

	private MbDepartmentDto department;
}
