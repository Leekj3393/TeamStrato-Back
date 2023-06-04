package com.strato.skylift.approval.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Member;


public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>  {

	@EntityGraph(attributePaths= {"accessor", "approval"})
	Page<ApprovalLine> findByAccessorAndAppPriorYnAndAppTime(Pageable pageable, Member accessor, String appPriorYn,
			Date appTime);


	@EntityGraph(attributePaths= {"accessor", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.approval.appCode = :appCode")
	List<ApprovalLine> findAllByApproval(Long appCode);

	
	@EntityGraph(attributePaths= {"accessor", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.appOrder = :appOrder AND a.appPriorYn = :appPriorYn")
	Optional<ApprovalLine> findByAppOrderAndAppPriorYn(Long appOrder, String appPriorYn);


	@EntityGraph(attributePaths= {"accessor", "approval"})
	ApprovalLine findByAppOrderAndAppLineStatus(Long nextOrder, String appLineStatus);


	@EntityGraph(attributePaths= {"accessor", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.approval.appCode = :appCode AND a.accessor.memberCode = :memberCode")
	ApprovalLine findAppLineByAppCodeAndAccessorMemberCode(Long appCode, Long memberCode);

// 결재코드와 결재선으로 지정된 직원 코드로 결재선 조회
	@Query("SELECT al FROM ApprovalLine al JOIN fetch al.accessor JOIN fetch al.approval "
			+ "WHERE al.accessor.memberCode = :memberCode "
			+ "AND al.approval.appCode = :appCode")
	ApprovalLine findbyAccessorAndApproval(Long memberCode, Long appCode);


	// 다음 결재자 찾기
	@Query("SELECT al FROM ApprovalLine al JOIN fetch al.approval "
			+ "WHERE al.appOrder = :nextOrder "
			+ "AND al.approval.appCode = :appCode")
	ApprovalLine findByApprovalAndAppOrder(Long appCode, Long nextOrder);
}
