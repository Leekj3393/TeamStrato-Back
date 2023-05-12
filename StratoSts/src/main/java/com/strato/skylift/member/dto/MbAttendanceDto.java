package com.strato.skylift.member.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MbAttendanceDto {

    private Long attendanceCode;
    private Long member;
    private String Status;
    private Date attendanceDate;
    private Date startTime;
    private Date endTime;
    private Date outTime;
    private Date returnTime;

}
