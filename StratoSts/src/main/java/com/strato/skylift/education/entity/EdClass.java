package com.strato.skylift.education.entity;

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

import com.strato.skylift.entity.Education;
import com.strato.skylift.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_CLASS")
@DynamicInsert
@SequenceGenerator(name="CLASS_SEQ_GENERATOR",
			sequenceName="SEQ_CLASS",
			initialValue=1, allocationSize=1)
public class EdClass {
	
	@Id
	@Column(name="CLASS_CODE")  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLASS_SEQ_GENERATOR")
	private Long classCode;  
	
	@Column(name="CLASS_TIME")
	private Long classTime;
	
	@Column(name="CLASS_STATUS")
	private String classStatus;
	
	@Column(name="CLASS_START")
	private Date classStart;
	
	@Column(name="CLASS_END")
	private Date classEnd;
	
	@Column(name="CLASS_VIEW")
	private String classView;
	
	@Column(name="CLASS_PERCENT")
	private Long classPercent;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_CODE")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="ED_CODE")
	private Education education;
	
	/* 수강 업데이트*/
	public void update(Long classTime, Date classEnd, String classStatus, String classView, Long classPercent) {
		this.classTime = classTime;
		this.classEnd = classEnd;
		this.classStatus = classStatus;
		this.classView = classView;
		this.classPercent = classPercent;
	}
	
}
