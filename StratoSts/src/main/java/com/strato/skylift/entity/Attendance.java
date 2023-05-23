package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_ATTENDANCE")
@SequenceGenerator(name = "ATTENDANCE_SEQ_GENERATOR",
                  sequenceName = "SEQ_ATTENDANCE",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
public class Attendance
{
    @Id @Column(name = "ATTENDANCE_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTENDANCE_SEQ_GENERATOR")
    private Long attendanceCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ATTENDANCE_DATE")
    private Date attendanceDate;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "OUT_TIME")
    private Date outTime;

    @Column(name = "RETURN_TIME")
    private Date returnTime;

    // Update the last attendance date based on the attendance record


}
