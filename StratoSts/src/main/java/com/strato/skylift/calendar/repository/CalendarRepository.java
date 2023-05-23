package com.strato.skylift.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

	List<Calendar> findCalendarByMemberCode(Long memberCode);

	List<Calendar> findCalendarByDeptCodeAndDivision(String deptCode, String division);
	

}