package com.strato.skylift.salary.repository;

import com.strato.skylift.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalAttendanceRepository extends JpaRepository<Attendance , Long>
{
    @Query(value = "SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN to_date(:day , 'yyyy-MM-dd') AND last_day(to_date(:day , 'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode " +
            "ORDER BY a.attendanceDate")
    Page<Attendance> findByMemeberCodeLikeDay(Long memberCode, String day , Pageable pageable);

    @Query(value = "SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN to_date(:day , 'yyyy-MM-dd') AND last_day(to_date(:day , 'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode " +
            "ORDER BY a.attendanceDate")
    List<Attendance> findByDay(Long memberCode , String day);

    @Query(value = "SELECT COUNT(a.startTime) FROM Attendance a WHERE a.attendanceDate " +
            "BETWEEN to_date(:day, 'yyyy-MM-dd') AND last_day(to_date(:day,'yyyy-MM-dd')) " +
            "AND a.member.memberCode = :memberCode " +
            "AND to_char(a.startTime,'HH24:MI') >= '09:01' " +
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
