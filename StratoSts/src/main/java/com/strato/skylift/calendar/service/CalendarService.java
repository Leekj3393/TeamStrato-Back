package com.strato.skylift.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.strato.skylift.calendar.dto.CalendarDto;
import com.strato.skylift.calendar.repository.CalendarRepository;
import com.strato.skylift.entity.Calendar;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalendarService {
	
	private final CalendarRepository calendarRepository;
	private final ModelMapper modelMapper;
	
	public CalendarService(CalendarRepository calendarRepository, ModelMapper modelMapper) {
		this.calendarRepository = calendarRepository;
		this.modelMapper = modelMapper;
	}
	
	// 1. 개인 일정 조회
	public List<CalendarDto> findCalendarByMemberCode(Long memberCode){
		
		log.info("[CalendarService] memberCode : {}", memberCode);
		
		List<Calendar> calendarList = calendarRepository.findCalendarByMemberCode(memberCode);
		
		List<CalendarDto> calendarDto = calendarList.stream().map(calendar -> modelMapper.map(calendar, CalendarDto.class)).collect(Collectors.toList());
		
		return calendarDto;
	}
	
	// 2. 부서 일정 조회
	public List<CalendarDto> findCalendarByDeptCodeAndDivision(String deptCode, String division){
		
		log.info("[CalendarService] deptCode : {}", deptCode);
		
		List<Calendar> calendarList = calendarRepository.findCalendarByDeptCodeAndDivision(deptCode, division);
		
		List<CalendarDto> calendarDto = calendarList.stream().map(calendar -> modelMapper.map(calendar, CalendarDto.class)).collect(Collectors.toList());
		
		return calendarDto;
	}

}
