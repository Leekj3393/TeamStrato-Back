package com.strato.skylift.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class MbFileDto {
	
	private Long fileCode;

	private String fileName;

	private String filePath;

	private String fileType;

	private Long memberCode;

}
