package com.strato.skylift.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Member;


public interface AppMemberRepository extends JpaRepository<Member, Long>{

	//결재선 선택을 위해서 멤버 목록 조회
//	List<Member> findAll();

	
	
}
