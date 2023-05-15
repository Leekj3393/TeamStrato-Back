package com.strato.skylift.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TBL_DEPARTMENT")
public class Department {
	
	@Id
	@Column(name="DEPT_CODE")
	private String deptCode;
	
	@Column(name="DEPT_NAME")
	private String deptName;
	
}
