package com.strato.skylift.salary.dto;

import com.strato.skylift.member.dto.MbMemberDto;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class TotalAttendanceDTO
{
    private Long totalTime;
    private Long overTime;
    private Long late;
    private Long out;
    private Long earlyLeave;
    private Long absence;
    private MbMemberDto member;
    private List<AttendanceDTO> attendance;
}
