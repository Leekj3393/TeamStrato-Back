package com.strato.skylift.salary.util;

import com.strato.skylift.salary.dto.AttendanceDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
public class Calculator
{
    public Long Conversion(AttendanceDTO attendance)
    {
        Date s = attendance.getStartTime();
        Date e = attendance.getEndTime();

        LocalDateTime sTime = s.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime eTime = e.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if(sTime.getHour() < 9)
        {
            sTime = LocalDateTime.of(sTime.getYear(),sTime.getMonth(),sTime.getDayOfMonth() , 9 , 0);
        }

        int sHour = sTime.getHour();
        int sMinute = sTime.getMinute();
        int eHour = eTime.getHour();
        int eMinute = eTime.getMinute();
        int start = (sHour * 60) + sMinute;
        int end = (eHour * 60) + eMinute;

        Long result = ((long) Math.abs(start - end));
       return result;
    }

    public Long overTime(Long time)
    {
        int over = 480;
        Long overWork = time - over;

        if(overWork <= 0)
            return 0L;
        else
            return overWork;
    }

    public Long totalTime(List<AttendanceDTO> attendance)
    {
        Long result = 0L;
        for(AttendanceDTO a : attendance)
        {
            result += a.getWorkTime();
        }
        log.info("result : " + result);
        return result;
    }

    public Long totalOverTime(List<AttendanceDTO> attendance)
    {
        Long result = 0L;
        for(AttendanceDTO a : attendance)
        {
            result += a.getOverWorkTime();
            log.info("Over result : " + result);
        }
        return result;
    }


    public Long income(Long s)
    {
        if(s < 2000000)
            return Math.round(s * 0.01);
        else if(s < 3000000)
            return Math.round(s * 0.02);
        else if(s < 4000000)
            return Math.round(s * 0.03);
        else if(s < 5000000)
            return Math.round(s * 0.05);
        else if(s < 6000000)
            return Math.round(s * 0.07);
        else
            return Math.round(s * 0.09);
    }

    public Long empInsurance(Long s)
    {
        log.info("empInsurance : " + (s * 0.008));
        return Math.round(s * 0.008);
    }

    public Long nationalPension(Long s)
    {
        log.info("nationalPension : " + (s * 0.045));
        return Math.round(s * 0.045);
    }

    public Long medicalInsurance(Long s)
    {
        log.info("medicalInsurance : " + (s * 0.0343));
        return Math.round(s * 0.0343);
    }

    public Long allowance (Long s , Long time)
    {
        Long allow = s / 20 / 8 / 6;
        Long t = ((long)(Math.floor(time / 10) * 10));
        Long m = allow * t;
        return Math.round(m + 1.5);
    }
}
