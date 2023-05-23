package com.strato.skylift.calendar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.strato.skylift.calendar.dto.CalendarDto;
import com.strato.skylift.calendar.service.CalendarService;
import com.strato.skylift.common.ResponseDto;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

	private final CalendarService calendarService;
	
	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	@PostMapping("/personal")
	public ResponseEntity<ResponseDto> findCalendarByMemberCode(@RequestBody CalendarDto calendarDto){
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "캘린더 조회 성공", calendarService.findCalendarByMemberCode(calendarDto.getMemberCode())));
	}
	
	@PostMapping("/dept")
	public ResponseEntity<ResponseDto> findCalendarByDeptCode(@RequestBody CalendarDto calendarDto){
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "부서 캘린더 조회 성공", calendarService.findCalendarByDeptCodeAndDivision(calendarDto.getDeptCode(),calendarDto.getDivision())));
	}
}
