package com.strato.skylift.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.Member;

public interface MemberRepository extends JpaRepository <Member, Long> {

	/* 직원 전체목록 조회(공통) */
	@EntityGraph(attributePaths= {"department", "job"})
	Page<Member> findAll(Pageable pageable);
	
	/* 직원 검색 조회(이름) */
	@EntityGraph(attributePaths= {"department", "job"})
	@Query("SELECT m FROM Member m WHERE m.memberName LIKE %:memberName%")
	Page<Member> findByMemberName(Pageable pageable, @Param("memberName") String MemberName);
	
	/* 직원 검색 조회(사번) */
	@EntityGraph(attributePaths= {"department", "job"})
	@Query("SELECT m FROM Member m WHERE m.memberCode = :memberCode")
	Page<Member> findByMemberCode(Pageable pageable, @Param("memberCode") Long memberCode);
	
	/* 직원 검색 조회(부서명) */
	@EntityGraph(attributePaths= {"department", "job"})
	@Query(value = "SELECT m " +
		   "  FROM Member m " + 
		   "  JOIN m.department d " + 
		   " WHERE d.deptName = :deptName")
	Page<Member> findByDeptName(Pageable pageable, @Param("deptName") String deptName);
	
	/* 직원 검색 조회(직급명) */
	@EntityGraph(attributePaths= {"department", "job"})
	@Query(value = "SELECT m " +
			   "  FROM Member m " + 
			   "  JOIN m.job j " + 
			   " WHERE j.jobName = :jobName")
	Page<Member> findByJobName(Pageable pageable, String jobName);
	
	/* 직원 상세정보 조회(공통) -> findById */
	Optional<Member> findByMemberId(String memberId);

	/* Id찾기 */
	Optional<Member> findByMemberNameAndResidentNo(String memberName, String residentNo);
	
	/* 비밀번호 변경창 용 */
	Optional<Member> findByMemberIdAndMemberPwd(String memberId, String memberPwd);

	Optional<Member> findMemberIdByResidentNo(String residentNo);

//	Optional<Member> updateMemberPwdByMemberId(String memberId, String memberPwd);
  
}
