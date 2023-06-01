package com.strato.skylift.approval.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Member;


public interface AppMemberRepository extends JpaRepository<Member, Long>{


	//결재 시 본인인증을 위해서 직원상세정보 조회
	Optional<Member> findByMemberId(String memberId);
	
	
}
