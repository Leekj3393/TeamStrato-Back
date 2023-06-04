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
@Table(name = "TBL_APP_LINE")
@SequenceGenerator(name = "APPLINE_SEQ_GENERATOR",
                   sequenceName = "SEQ_APP_LINE",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert
public class ApprovalLine {
	
    @Id @Column(name = "APP_LINE_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLINE_SEQ_GENERATOR")
	private Long appLineCode;
    
	@ManyToOne
	@JoinColumn(name = "APP_CODE")
	private Approval approval;
	
	@Column(name = "APP_LINE_STATUS")
	private String appLineStatus;
	
	@Column(name = "APP_PRIOR_YN")
	private String appPriorYn;

	@Column(name = "APP_TIME")
	private String appTime;
	
	@Column(name = "APP_ORDER")
	private Long appOrder;
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_CODE")
	private Member accessor;

	/*결재선 승인/반려 용도의 메소드를 별도로 정의함*/
	public void update(String appPriorYn, String appLineStatus, String appTime) {
		this.appPriorYn = appPriorYn;
		this.appLineStatus = appLineStatus;
		this.appTime = appTime;
	}
	

}
