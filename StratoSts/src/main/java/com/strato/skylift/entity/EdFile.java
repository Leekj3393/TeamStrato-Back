package com.strato.skylift.entity;

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
@Table(name="TBL_FILE")
@SequenceGenerator(name="FILE_SEQ_GENERATOR",
			sequenceName="SEQ_FILE",
			initialValue=1, allocationSize=1)
public class EdFile {
	
	@Id
	@Column(name="FILE_CODE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FILE_SEQ_GENERATOR")
	private Long fileCode;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="FILE_PATH")
	private String filePath;
	
	@Column(name="FILE_TYPE")
	private String fileType;
	
	@Column(name="ED_CODE")
	private Long edCode;

}
