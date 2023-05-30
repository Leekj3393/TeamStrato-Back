package com.strato.skylift.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.File;
import com.strato.skylift.entity.MbFile;
import com.strato.skylift.entity.Member;

public interface MbFileRepository extends JpaRepository <MbFile, Long> {
	
	@Query("SELECT f " +
		   "  FROM MbFile f " +
		   " WHERE f.memberCode = :memberCode "
			)
	MbFile findByMemberCode(@Param("memberCode") Long memberCode);
	
	/* 교육 사진 조회 */
	@Query("SELECT f " +
			   "  FROM MbFile f " +
			   " WHERE f.fileType = '교육사진'"
				)
	Page<MbFile> findByFileType(Pageable pageable);
}
