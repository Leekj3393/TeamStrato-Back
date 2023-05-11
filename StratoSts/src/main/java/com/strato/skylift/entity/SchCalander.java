package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_SCH_CALANDER")
@SequenceGenerator(name = "CALANDER_SEQ_GENERATOR",
                  sequenceName = "SEQ_SCH_CALANDER",
                  initialValue = 1 , allocationSize = 0)
@Getter @Setter
public class SchCalander
{
    @Id @Column(name = "CALENDAR_CODE")
    private Long calendarCode;

    @ManyToOne
    @JoinColumn(name = "DEPT_CODE")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_NO")
    private SchCategory schCategory;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @Column(name = "DIVISION")
    private String division;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "REGIST_TIME")
    private Date registTime;

    @Column(name = "DELETE_TIME")
    private Date deleteTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "DELETE_YN")
    private String deleteYn;

}
