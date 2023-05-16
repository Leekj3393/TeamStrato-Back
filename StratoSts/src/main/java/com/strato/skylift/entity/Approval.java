package com.strato.skylift.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_APPROVAL")
@SequenceGenerator(name = "APPROVAL_SEQ_GENERATOR",
                   sequenceName = "SEQ_APPROVAL",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
public class Approval
{
    @Id @Column(name = "APP_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPROVAL_SEQ_GENERATOR")
    private Long appCode;
    
    @ManyToOne
    @JoinColumn(name ="MEMBER_CODE")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "REQUEST_CODE")
    private Request request;

    @Column(name = "APP_TITLE")
    private String appTitle;

    @Column(name = "APP_CONTENT")
    private String appContent;

    @Column(name = "APP_TYPE")
    private String appType;

    @Column(name = "APP_STATUS")
    private String appStatus;

    @Column(name = "APP_REGIST_DATE")
    private Date appRegistDate;

    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "APP_WDL_DATE")
    private Date appWdlDate;

}
