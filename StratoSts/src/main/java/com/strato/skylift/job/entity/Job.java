package com.strato.skylift.job.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TBL_JOB")
public class Job {
	
	@Id
	@Column(name="JOB_CODE")
	private String jobCode;
	
	@Column(name="JOB_NAME")
	private String jobName;
}
