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
@Table(name="TBL_EDUCATION")
@SequenceGenerator(name="ED_SEQ_GENERATOR",
			sequenceName="SEQ_EDUCATION",
			initialValue=1, allocationSize=1)
public class Education {
	
	@Id
	@Column(name="ED_CODE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ED_SEQ_GENERATOR")
	private Long edCode;
	
	@Column(name="ED_NAME")
	private String edName;
	
	@Column(name="ED_TIME")
	private Long edTime;
	
	@Column(name="ED_STATUS")
	private String edStatus;
	
	@Column(name="ED_TYPE")
	private String edType;
	
	
}
