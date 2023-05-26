package com.strato.skylift.education.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.strato.skylift.education.entity.EdClass;
import com.strato.skylift.entity.Member;

public interface ClassRepository extends JpaRepository <EdClass, Long> {
	
	/* 직원정보와 교육정보로 수강정보 조회 */
	@Query("SELECT c FROM EdClass c JOIN fetch c.member JOIN fetch c.education WHERE c.member.memberCode = :memberCode AND c.education.edCode = :edCode")
	EdClass findByMemberAndEducation(Long memberCode, Long edCode);
	
	/* 직원정보로 수강정보 조회 */
	@Query("SELECT c FROM EdClass c JOIN fetch c.member WHERE c.member.memberCode = :memberCode AND c.classView = 'Y'")
	List<EdClass> findByMemberCodeList(Long memberCode);

	/* 직원정보로 수강정보 조회(페이징) */
	@EntityGraph(attributePaths= {"member", "education", "member.job", "member.department", "member.memberRole", "member.files"})
	Page<EdClass> findByMember(Pageable pageable, Member findMember);

	/* 직원정보로 수강정보 조회 */
	@Query("SELECT c FROM EdClass c JOIN fetch c.member JOIN fetch c.education WHERE c.member.memberCode = :memberCode AND c.classView = 'Y' AND c.education.edCode = :edCode")
	EdClass findByMemberCode(Long memberCode, Long edCode);
	
	
	
}
