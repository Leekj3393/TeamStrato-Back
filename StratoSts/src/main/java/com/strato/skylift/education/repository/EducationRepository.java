package com.strato.skylift.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.Education;

public interface EducationRepository extends JpaRepository <Education, Long> {
	
	/* 전체 교육 조회 */
	Page<Education> findAll(Pageable pageable);
	
	/* 안전교육 조회 */
	@Query(value = "SELECT e " +
				   "  FROM Education e " +
				   " WHERE e.edType = '안전'")
	Page<Education> findByEdTypeSafety(Pageable pageable);
	
	/* 직무교육 조회 */
	@Query(value = "SELECT e " +
			   "  FROM Education e " +
			   " WHERE e.edType = '직무'")
	Page<Education> findByEdTypeDuty(Pageable pageable);
	
	/* 기타교육 조회 */
	@Query(value = "SELECT e " +
			   "  FROM Education e " +
			   " WHERE e.edType = '기타'")
	Page<Education> findByEdTypeOther(Pageable pageable);
	
	/* 교육 코드로 교육 조회 */
	@Query(value="SELECT e FROM Education e WHERE e.edCode = :edCode" )
	Object findByEdCode(@Param("edCode")Long edCode);
}
