package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_NOTICE")
@Getter @Setter
@SequenceGenerator(name="NOTICE_SEQ_GENERATOR",
        sequenceName="SEQ_MEMBER",
        initialValue=1, allocationSize=1)
public class Notice
{
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "NOTICE_SEQ_GENERATOR")
    @Id @Column(name = "NOTICE_CODE")
    private Long noticeCode;

    @Column(name = "NOTICE_TITLE")
    private String noticeTitle;

    @Column(name = "NOTICE_CONTENT")
    private String noticeContent;

    @Column(name = "NOTICE_TYPE")
    private String noticeType;

    @Column(name = "NOTICE_DEL_YN")
    private String noticeDelYn;

    @Column(name = "NOTICE_STATUS")
    private String noticeStatus;

    @Column(name = "NOTICE_REGIST_DATE")
    private Date noticeRegistDate;

    @Column(name = "NOTICE_INIT_DATE")
    private Date noticeInitDate;

    @Column(name = "NOTICE_END_DATE")
    private Date noticeEndDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "DEPT_CODE")
    private Department department;
}
