package com.strato.skylift.entity;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_APPROVAL")
@SequenceGenerator(name = "APPROVAL_SEQ_GENERATOR",
        sequenceName = "SEQ_APPROVAL",
        initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appCode")
public class Approval
{
    @Id @Column(name = "APP_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPROVAL_SEQ_GENERATOR")
    private Long appCode;

    @ManyToOne(fetch = FetchType.EAGER)
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
