package com.strato.skylift.salary.repository;

import com.strato.skylift.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalAttendanceRepository extends JpaRepository<Attendance , Long>
{
    @Query(value = "SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN to_date(:day , 'yyyy-MM-dd') AND last_day(to_date(:day , 'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode " +
            "ORDER BY a.attendanceDate")
    List<Attendance> findByMemeberCodeLikeDay(Long memberCode,String day);

    @Query(value = "SELECT COUNT(a.startTime) FROM Attendance a WHERE a.attendanceDate " +
            "BETWEEN to_date(:day, 'yyyy-MM-dd') AND last_day(to_date(:day,'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode " +
            "AND a.startTime > to_date('09:01','HH24:MI') " +
            "GROUP BY a.member.memberCode ")
    Long countByLate(Long memberCode, String day);

    @Query(value = "SELECT COUNT(a.outTime) FROM Attendance a WHERE a.attendanceDate " +
            "BETWEEN to_date(:day, 'yyyy-MM-dd') AND last_day(to_date(:day,'yyyy-MM-dd')) " +
            "AND a.outTime != null " +
            "AND a.member.memberCode = :memberCode " +
            "group by a.member.memberCode")
    Long countByOut(Long memberCode, String day);

    @Query(value = "SELECT COUNT(a.endTime) FROM Attendance a WHERE a.attendanceDate " +
            "BETWEEN to_date(:day, 'yyyy-MM-dd') AND last_day(to_date(:day,'yyyy-MM-dd')) " +
            "AND to_char(a.endTime,'HH24') - to_char(a.startTime,'HH24') < 7 " +
            "AND a.member.memberCode = :memberCode " +
            "group by a.member.memberCode")
    Long countByearlyLeave(Long memberCode, String day);

    @Query(value = "SELECT COUNT(a.status) FROM Attendance a WHERE a.attendanceDate " +
            "BETWEEN to_date(:day, 'yyyy-MM-dd') AND last_day(to_date(:day,'yyyy-MM-dd')) " +
            "AND a.status = '결근' " +
            "AND a.member.memberCode = :memberCode " +
            "group by a.member.memberCode")
    Long countByabsence(Long memberCode, String day);
}
