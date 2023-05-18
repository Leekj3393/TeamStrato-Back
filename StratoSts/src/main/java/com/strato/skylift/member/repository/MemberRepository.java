package com.strato.skylift.member.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;

public interface MemberRepository extends JpaRepository <Member, Long> {
	

	/* 직원 전체목록 조회(공통) */
//	@EntityGraph(attributePaths= {"department", "job"})
	Page<Member> findAll(Pageable pageable);
	
	/* 직원 검색 조회(아이디) */
	Page<Member> findByMemberId(Pageable pageable, String MemberId);
	
	/* 직원 검색 조회(이름) */
	Page<Member> findByMemberName(Pageable pageable, String MemberName);
	
	/* 직원 상세정보 조회(공통) -> findById */
	Optional<Member> findByMemberId(String memberId);

	/* Id찾기 */
	Optional<Member> findByMemberNameAndResidentNo(String memberName, String residentNo);
	
//	/* Pwd 변경 */
	Optional<Member> findMemberIdByResidentNo(String residentNo);
//	Optional<Member> updateMemberPwdByMemberId(String memberId, String memberPwd);
	
	
	
}
