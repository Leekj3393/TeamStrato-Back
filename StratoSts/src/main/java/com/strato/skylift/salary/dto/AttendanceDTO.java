package com.strato.skylift.salary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendanceDTO
{
    private Long attendanceCode;
    private SalMemberDTO member;
    private String status;
    @JsonFormat(pattern = "yy-MM-dd")
    private Date attendanceDate;
    @JsonFormat(pattern = "HH:mm" , timezone = "Asia/Seoul")
    private Date startTime;
    @JsonFormat(pattern = "HH:mm" , timezone = "Asia/Seoul")
    private Date endTime;
    @JsonFormat(pattern = "HH:mm" , timezone = "Asia/Seoul")
    private Date outTime;
    @JsonFormat(pattern = "HH:mm" , timezone = "Asia/Seoul")
    private Date returnTime;

    private Long workTime;
    private Long overWorkTime;

}
