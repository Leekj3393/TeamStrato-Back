package com.strato.skylift.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_SCH_CALANDER") // 여기도 테이블명 오타 수정시 수정 필요
@SequenceGenerator(name="CALENDAR_SEQ_GENERATOR",
			sequenceName="SEQ_SCH_CALANDER", // 시퀀스명 오타 수정시 해당부분 수정 필요
			initialValue=1, allocationSize=1)
public class Calendar {

	@Id
	@Column(name="CALENDAR_CODE")  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CALENDAR_SEQ_GENERATOR")
	private Long calendarCode;
	
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	
	@Column(name="MEMBER_CODE")
	private Long memberCode;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="NAME")
	private String title;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="START_TIME")
	private Date start;
	
	@Column(name="END_TIME")
	private Date end;
	
	@Column(name="REGIST_TIME")
	private Date registTime;
	
	@Column(name="UPDATE_TIME")
	private Date updateTime;
	
	@Column(name="CATEGORY_COLOR")
	private String color;
	
	
}
