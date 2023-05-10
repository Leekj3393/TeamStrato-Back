package com.strato.skylift.member.entity;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.strato.skylift.department.entity.Department;
import com.strato.skylift.job.entity.Job;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_MEMBER")
@SequenceGenerator(name="MEMBER_SEQ_GENERATOR",
			sequenceName="SEQ_MEMBER_CODE",
			initialValue=1, allocationSize=1)
public class Member {
	
	@Id
	@Column(name="MEMBER_CODE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEMBER_SEQ_GENERATOR")
	private Long memberCode;
	
	@Column(name="MEMBER_ID")
	private String memberId;
	
	@Column(name="MEMBER_PWD")
	private String memberPwd;
	
	@Column(name="MEMBER_NAME")
	private String memberName;
	
	@Column(name="RESIDENT_NO")
	private String residentNo;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="PHONE")
	private String phone;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="MEMBER_STATUS")
	private String memberStatus;
	
	@Column(name="MEMBER_HIRE_DATE")
	private Date memberHireDate;
	
	@Column(name="BANK_NAME")
	private String bankName;
	
	@Column(name="BANK_NO")
	private String bankNo;
	
	@Column(name="MEMBER_SALARY")
	private Long memberSalary;
	
	@Column(name="MEMBER_ANNUAL")
	private Long memberAnnual;
	
	@ManyToOne
	@JoinColumn(name="JOB_CODE")
	private Job job;
	
	@ManyToOne
	@JoinColumn(name="DEPT_CODE")
	private Department department;
	
}
