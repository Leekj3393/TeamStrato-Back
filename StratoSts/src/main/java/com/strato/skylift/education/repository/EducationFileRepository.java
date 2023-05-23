package com.strato.skylift.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.EdFile;


public interface EducationFileRepository extends JpaRepository <EdFile, Long>{
	
	/* 교육 동영상 조회 */
	@Query("SELECT f " +
		   "  FROM EdFile f " +
		   " WHERE f.edCode = :edCode "
				)
	EdFile findByEdCode(@Param("edCode") Long edCode);

}
