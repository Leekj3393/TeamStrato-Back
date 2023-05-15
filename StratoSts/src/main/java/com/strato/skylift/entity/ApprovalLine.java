package com.strato.skylift.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_APP_LINE")
@SequenceGenerator(name = "APPLINE_SEQ_GENERATOR",
                   sequenceName = "SEQ_APP_LINE",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
public class ApprovalLine {
	
    @Id @Column(name = "APP_LINE_CODE")
	private Long appLineCode;
    
	@ManyToOne
	@JoinColumn(name = "APP_CODE")
	private Approval approval;
	
	@Column(name = "APP_STATUS")
	private String appStatus;
	
	@Column(name = "APP_PRIOR_YN")
	private String appPriorYn;

	@Column(name = "APP_TIME")
	private Date appTime;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_CODE")
	private Member member;
	

}
