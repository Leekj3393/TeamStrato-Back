package com.strato.skylift.calendar.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDto {

	private Long calendarCode;
	private String deptCode;
	private Long memberCode;
	private String division;
	private String title;
	private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date start;
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date end;
	private Date registTime;
	private Date updateTime;
	private String color;
}
