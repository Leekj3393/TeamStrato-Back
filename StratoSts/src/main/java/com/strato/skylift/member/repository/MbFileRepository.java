package com.strato.skylift.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.File;
import com.strato.skylift.entity.MbFile;

public interface MbFileRepository extends JpaRepository <MbFile, Long> {
	
	@Query("SELECT f " +
		   "  FROM MbFile f " +
		   " WHERE f.memberCode = :memberCode "
			)
	MbFile findByMemberCode(@Param("memberCode") Long memberCode);
	
}
