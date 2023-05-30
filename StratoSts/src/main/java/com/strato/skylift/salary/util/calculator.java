package com.strato.skylift.salary.util;

import com.strato.skylift.salary.dto.AttendanceDTO;

public class calculator
{
    public Long Conversion(AttendanceDTO attendance)
    {
        int sHour = attendance.getStartTime().getHour();
        int sMinute = attendance.getStartTime().getMinute();
        int eHour = attendance.getEndTime().getHour();
        int eMinute = attendance.getEndTime().getMinute();
        int start = (sHour * 60) + sMinute;
        int end = (eHour * 60) + eMinute;
       return ((long) Math.abs(start - end));
    }


    public double income(Long s)
    {
        if(s < 20000000)
            return 0;
        else if(s < 3000000)
            return s * 0.01;
        else if(s < 40000000)
            return s * 0.03;
        else if(s < 50000000)
            return s * 0.05;
        else if(s < 6000000)
            return s * 0.07;
        else
            return s * 0.09;
    }

    public double empInsurance(Long s)
    {
        return s * 0.008;
    }

    public double nationalPension(Long s)
    {
        return s * 0.045;
    }

    public double medicalInsurance(Long s)
    {
        return s * 0.0343;
    }

    public Long overTime(Long time)
    {
        int over = 480;
        Long overWork = time - over;

        if(overWork < 0)
            return null;
        else
            return overWork;
    }

    public double allowance(Long s , Long time)
    {
        Long allow = s / 20 / 8 / 6;
        Long t = ((long)(Math.floor(time / 10) * 10));
        Long m = allow * t;
        return m * 1.5;
    }
}
