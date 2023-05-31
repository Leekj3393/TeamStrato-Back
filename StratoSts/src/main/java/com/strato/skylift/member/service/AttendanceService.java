package com.strato.skylift.member.service;

import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Request;
import com.strato.skylift.member.repository.AttendanceRepository;
import com.strato.skylift.member.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceService {

    private final MyPageRepository myPageRepository;
    private final ModelMapper modelMapper;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public Attendance setAbsenteeismStatus(Long attendanceCode) {
        Optional<Attendance> attendanceOptional = attendanceRepository.findByAttendanceCode(attendanceCode);

        if (attendanceOptional.isPresent()) {
            Attendance attendance = attendanceOptional.get();
            attendance.setStatus("결근");
            return attendanceRepository.save(attendance);
        } else {
            return null;
        }
    }

    public Attendance managerEndTime(Long attendanceCode) {
        Optional<Attendance> attendanceOptional = attendanceRepository.findByAttendanceCode(attendanceCode);

        if (attendanceOptional.isPresent()) {
            Attendance attendance = attendanceOptional.get();
            attendance.setStatus("관리자권한 퇴근");
            return attendanceRepository.save(attendance);
        } else {
            return null;
        }
    }


    public Attendance managerLazyTime(Long attendanceCode) {
        Optional<Attendance> attendanceOptional = attendanceRepository.findByAttendanceCode(attendanceCode);

        if (attendanceOptional.isPresent()) {
            Attendance attendance = attendanceOptional.get();
            attendance.setStatus("지각");
            return attendanceRepository.save(attendance);
        } else {
            return null;
        }
    }

    public Attendance managerDeleteTime(Long attendanceCode) {
        Attendance attendance = attendanceRepository.findByAttendanceCode(attendanceCode).orElseThrow(() ->
                new IllegalArgumentException("근태 코드를 다시 확인하세요 :  " + attendanceCode));
        attendanceRepository.delete(attendance);
        return attendance;
    }

}
