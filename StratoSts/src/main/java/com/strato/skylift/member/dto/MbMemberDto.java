package com.strato.skylift.member.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.entity.MemberRole;

import lombok.Data;

@Data
public class MbMemberDto implements UserDetails {

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
	
	private MbMemberRoleDto memberRole;

	private MemberRole memberRole2;
	
	private MbFileDto memberFile;
	
	@JsonIgnore
	private MultipartFile memberImage;
	
	private Collection<? extends GrantedAuthority> authorities;
	private MbAttendanceDto attendance;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAttendance(MbAttendanceDto attendance) {
		this.attendance = attendance;
	}




}
