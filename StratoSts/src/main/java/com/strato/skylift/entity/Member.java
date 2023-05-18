package com.strato.skylift.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DynamicInsert
@Table(name="TBL_MEMBER")
@SequenceGenerator(name="MEMBER_SEQ_GENERATOR",
			sequenceName="SEQ_MEMBER",
			initialValue=1, allocationSize=1)
public class Member {

	@Id
	@Column(name = "MEMBER_CODE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
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

	@Column(name = "MEMBER_STATUS")
	private String memberStatus;

	@Column(name = "MEMBER_HIRE_DATE")
	private Date memberHireDate;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_NO")
	private String bankNo;

	@Column(name = "MEMBER_SALARY")
	private Long memberSalary;

	@Column(name = "MEMBER_ANNUAL")
	private Long memberAnnual;

	@ManyToOne
	@JoinColumn(name = "JOB_CODE")
	private Job job;

	@ManyToOne
	@JoinColumn(name = "DEPT_CODE")
	private Department department;

	@ManyToOne
	@JoinColumn(name = "ROLE_CODE")
	private MemberRole memberRole;

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MEMBER_CODE")
	private List<File> files = new ArrayList<>();
	
	/* 직원 정보 수정 */
	public void update(String memberName, String residentNo, String gender,
					   String phone, String address, String bankName, String bankNo,
					   Long memberSalary, Long memberAnnual) {

		this.memberName = memberName;
		this.residentNo = residentNo;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.bankName = bankName;
		this.bankNo = bankNo;
		this.memberSalary = memberSalary;
		this.memberAnnual = memberAnnual;
	}

	public void update(String bankName, String address, String phone, String bankNo) {
		this.bankName = bankName;
		this.address = address;
		this.phone = phone;
		this.bankNo = bankNo;
	}
	
	/* 직원 권한 부여 */
	public void update(MemberRole memberRole) {
		this.memberRole = memberRole;
	}
	
	/* 직원 인사이동 */
	public void update(Department department, Job job) {
		this.department = department;
		this.job = job;
    

}
  /* 임시비밀번호  */
	public void update(String memberPwd) {
		this.memberPwd = memberPwd;
	}
}