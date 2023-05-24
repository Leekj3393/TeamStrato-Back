package com.strato.skylift.education.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.education.entity.EdClass;

public interface ClassRepository extends JpaRepository <EdClass, Long> {
	
	/* 직원정보와 교육정보로 수강정보 조회 */
	@Query("SELECT c FROM EdClass c JOIN fetch c.member JOIN fetch c.education WHERE c.member.memberCode = :memberCode AND c.education.edCode = :edCode")
	EdClass findByMemberAndEducation(Long memberCode, Long edCode);
	
	/* 직원정보로 수강정보 조회 */
	@Query("SELECT c FROM EdClass c JOIN fetch c.member WHERE c.member.memberCode = :memberCode AND c.classView = 'Y'")
	EdClass findByMemberCode(Long memberCode);
	
	
	
}
