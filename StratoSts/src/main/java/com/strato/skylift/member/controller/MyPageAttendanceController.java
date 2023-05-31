package com.strato.skylift.member.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Notice;
import com.strato.skylift.member.repository.AttendanceRepository;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.member.service.AttendanceService;
import com.strato.skylift.member.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/skylift/myPage")
public class MyPageAttendanceController {

    private final MyPageService myPageService;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;

    @PostMapping("/workInfo/Absenteeism/{attendanceCode}")
    public ResponseEntity<ResponseDto> absenteeismAttendance(@PathVariable("attendanceCode") Long attendanceCode) {
        Attendance attendance = attendanceService.setAbsenteeismStatus(attendanceCode);

        if (attendance != null) {
            ResponseDto responseDto = new ResponseDto(HttpStatus.OK, "결근 변경 완료", attendance);
            return ResponseEntity.ok().body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }

    @PostMapping("/workInfo/managerEndTime/{attendanceCode}")
    public ResponseEntity<ResponseDto> managerEndTimeAttendance(@PathVariable("attendanceCode") Long attendanceCode) {
        Attendance attendance = attendanceService.managerEndTime(attendanceCode);

        if (attendance != null) {
            ResponseDto responseDto = new ResponseDto(HttpStatus.OK, "사용자 변경 퇴근 변경 완료", attendance);
            return ResponseEntity.ok().body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }

    @PostMapping("/workInfo/lazyTime/{attendanceCode}")
    public ResponseEntity<ResponseDto> lazyTimeAttendance(@PathVariable("attendanceCode") Long attendanceCode) {
        Attendance attendance = attendanceService.managerLazyTime(attendanceCode);

        if (attendance != null) {
            ResponseDto responseDto = new ResponseDto(HttpStatus.OK, "지각 변경 완료", attendance);
            return ResponseEntity.ok().body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }

    @DeleteMapping("/workInfo/deleteTime/{attendanceCode}")
    public ResponseEntity<ResponseDto> deleteTimeAttendance(@PathVariable("attendanceCode") Long attendanceCode) {
        Attendance attendance = attendanceService.managerDeleteTime(attendanceCode);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "멤버 근태 삭제 완료"));
    }

}
