package com.strato.skylift.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Member;

public interface MemberRepository extends JpaRepository <Member, Long> {

	/* 직원 전체목록 조회(공통) */
//	@EntityGraph(attributePaths= {"department", "job"})
	Page<Member> findAll(Pageable pageable);
	
	/* 직원 상세정보 조회(공통) -> findById */
	
	
}
