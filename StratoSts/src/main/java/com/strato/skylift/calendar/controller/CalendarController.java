package com.strato.skylift.calendar.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.strato.skylift.calendar.dto.CalendarDto;
import com.strato.skylift.calendar.service.CalendarService;
import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/calendar")
public class CalendarController {

	private final CalendarService calendarService;
	
	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	@PostMapping("/personal")
	public ResponseEntity<ResponseDto> findCalendarByMemberCode(@RequestBody CalendarDto calendarDto){
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "캘린더 조회 성공", calendarService.findCalendarByMemberCodeAndDivision(calendarDto.getMemberCode(),calendarDto.getDivision())));
	}
	
	@PostMapping("/dept")
	public ResponseEntity<ResponseDto> findCalendarByDeptCode(@RequestBody CalendarDto calendarDto){
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "부서 캘린더 조회 성공", calendarService.findCalendarByDeptCodeAndDivision(calendarDto.getDeptCode(),calendarDto.getDivision())));
	}
	
	@PostMapping("/insert")
	public ResponseEntity<ResponseDto> insertCalendar(@RequestBody CalendarDto calendarDto) {
		
		System.out.println(calendarDto);  
		
		calendarService.insertCalendar(calendarDto);
		
		log.info("calendarDto : {}", calendarDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "일정 등록 성공"));
	}
	
	@PostMapping("/update")
	public ResponseEntity<ResponseDto> updateCalendar(@RequestBody CalendarDto calendarDto) {
		
		System.out.println(calendarDto);  
		
		calendarService.updateCalendar(calendarDto);
		
		log.info("calendarDto : {}", calendarDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "일정 수정 성공"));
	}
	
	@PostMapping("/delete")
	public ResponseEntity<ResponseDto> deleteCalendar(@RequestBody CalendarDto calendarDto) {
		
		calendarService.deleteCalendarByCalendarCode(calendarDto.getCalendarCode());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "캘린더 삭제 성공"));
	}
	
    @GetMapping("/findall")
    public ResponseEntity<ResponseDto> find(@RequestParam(name = "page",defaultValue = "1")int page)
    {
        Page<CalendarDto> calendar = calendarService.findCalendalAll(page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(calendar);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(calendar.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }
}
