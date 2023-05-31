package com.strato.skylift.salary.repository;

import com.strato.skylift.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalAttendanceRepository extends JpaRepository<Attendance , Long>
{
    @Query(value = "SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN to_date(:day , 'yyyy-MM-dd') AND last_day(to_date(:day , 'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode")
    List<Attendance> findByMemeberCodeLikeDay(Long memberCode,String day);
}
