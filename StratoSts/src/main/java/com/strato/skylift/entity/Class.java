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

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="TBL_CLASS")
@SequenceGenerator(name="CLASS_SEQ_GENERATOR",
			sequenceName="SEQ_CLASS",
			initialValue=1, allocationSize=1)
public class Class {
	
	@Id
	@Column(name="CLASS_CODE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLASS_SEQ_GENERATOR")
	private Long classCode;
	
	@Column(name="CLASS_TIME")
	private Long classTime;
	
	@Column(name="CLASS_STATUS")
	private String classStatus;
	
	@Column(name="CLASS_DATE")
	private Date classDate;
	
	@Column(name="CLASS_END")
	private Date classEnd;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_CODE")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="ED_CODE")
	private Education education;
	
}
