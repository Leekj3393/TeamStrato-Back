package com.strato.skylift.approval.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Member;


public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>  {

//	@Query("SELECT a FROM ApprovalLine a WHERE a.memger.memberCode = :memberCode AND a.appPriorYn = :appPriorYn ")
//	Page<ApprovalLine> findByMemberAndAppPriorYn(Pageable pageable, Long memberCode, String appPriorYn);

	@EntityGraph(attributePaths= {"member", "approval"})
	Page<ApprovalLine> findByMemberAndAppPriorYn(Pageable pageable, Member member, String appPriorYn);

}
