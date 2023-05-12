package com.strato.skylift.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MbTokenDto {

	private String grantType;
	private String memberId;
	private String accessToken;
	private Long accessTokenExpiresIn;
	
}