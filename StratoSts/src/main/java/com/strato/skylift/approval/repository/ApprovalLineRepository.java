package com.strato.skylift.approval.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Member;


public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>  {

//	@Query("SELECT a FROM ApprovalLine a WHERE a.memger.memberCode = :memberCode AND a.appPriorYn = :appPriorYn ")
//	Page<ApprovalLine> findByMemberAndAppPriorYn(Pageable pageable, Long memberCode, String appPriorYn);

//	Page<ApprovalLine> findByMemberAndAppPriorYn(Pageable pageable, Member member, String appPriorYn);

	@EntityGraph(attributePaths= {"member", "approval"})
	Page<ApprovalLine> findByMemberAndAppPriorYnAndAppTime(Pageable pageable, Member member, String appPriorYn,
			Date appTime);

//	@EntityGraph(attributePaths= {"member", "approval"})
//	@Query("SELECT a FROM ApprovalLine a WHERE a.Approval.appCode = :appCode")
//	Optional<ApprovalLine> findByApproval(Long appCode);

	@EntityGraph(attributePaths= {"member", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.approval.appCode = :appCode")
	List<ApprovalLine> findByApproval(Long appCode);

	@EntityGraph(attributePaths= {"member", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.approval.appCode = :appCode")
	List<ApprovalLine> findAllByApproval(Long appCode);


//	@EntityGraph(attributePaths= {"member", "approval"})
//	Optional<ApprovalLine> findAllByAppCode(Long appCode);

}
