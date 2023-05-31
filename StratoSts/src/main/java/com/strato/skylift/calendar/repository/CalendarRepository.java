package com.strato.skylift.calendar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Calendar;
import com.strato.skylift.entity.EquCategory;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

	List<Calendar> findCalendarByDeptCodeAndDivision(String deptCode, String division);

	List<Calendar> findCalendarByMemberCodeAndDivision(Long memberCode, String division);

	List<Calendar> deleteCalendarByCalendarCode(Long calendarCode);
	
	Page<Calendar> findAll(Pageable pageable);
	

}
