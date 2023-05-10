package com.strato.skylift.member.dto;

import java.util.Date;

import com.strato.skylift.member.entity.Member;

import lombok.Data;

@Data
public class TransferDto {

	private Long transferCode;
	
	private Date transferDate; 
	
	private Member member;
	
	private Member job;
	
	private Member department;
	
}
