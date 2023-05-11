package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_APPROVAL")
@SequenceGenerator(name = "APPROVAL_SEQ_GENERATOR",
                   sequenceName = "SEQ_APPROVAL",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
public class Approval
{
    @Id @Column(name = "APP_CODE")
    private Long appCode;

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

    @Column(name = "APP_REGSIT_DATE")
    private Date appRegistDate;

    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "APP_WDL_DATE")
    private Date appWdlDate;

}
