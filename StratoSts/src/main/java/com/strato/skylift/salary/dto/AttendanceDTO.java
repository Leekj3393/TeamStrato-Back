package com.strato.skylift.salary.dto;

import com.strato.skylift.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO
{
    private Long attendanceCode;
    private Member member;
    private String status;
    private LocalDateTime attendanceDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime outTime;
    private LocalDateTime returnTime;

}
