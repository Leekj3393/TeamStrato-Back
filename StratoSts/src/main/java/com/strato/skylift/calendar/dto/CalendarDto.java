package com.strato.skylift.calendar.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDto {

	private Long calendarCode;
	private String deptCode;
	private Long categoryNo;
	private Long memberCode;
	private String division;
	private String title;
	private String content;
	private Date start;
	private Date end;
	private Date registTime;
	private Date updateTime;
	private String deleteYn;
	private String color;
}
