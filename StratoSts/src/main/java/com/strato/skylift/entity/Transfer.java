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

@Getter
@Setter
@Entity
@Table(name="TBL_TRANSFER")
@SequenceGenerator(name="TRANSFER_SEQ_GENERATOR",
	sequenceName="SEQ_TRANSFER_CODE",
	initialValue=1, allocationSize=1)
public class Transfer {
	
	@Id
	@Column(name="TRANSFER_CODE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSFER_SEQ_GENERATOR")
	private Long transferCode;
	
	@Column(name="TRANSFER_DATE")
	private Date transferDate; 
	
	@ManyToOne
	@JoinColumn(name="MEMBER_CODE")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="JOB_CODE")
	private Member job;
	
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_CODE")
	private Member department;
	
}
