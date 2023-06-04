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


//	@Query("SELECT a FROM ApprovalLine a WHERE a.accessor.memberCode = :memberCode AND a.appPriorYn = :appPriorYn")
//	@EntityGraph(attributePaths= {"accessor", "approval"})
//	Optional<ApprovalLine> findByAccessorAndAppPriorYn(Member accessor, String appPriorYn);

//	@EntityGraph(attributePaths= {"accessor", "approval"})
//	Optional<ApprovalLine> findByIdAndAppPriorYn(Long appLineCode, String appPriorYn);


//	@EntityGraph(attributePaths= {"accessor", "approval"})
//	Optional<ApprovalLine> findByAccessorAndAppPriorYn(Member accessor, String appPriorYn);


//	@EntityGraph(attributePaths= {"accessor", "approval"})
//	Optional<ApprovalLine> findByApprovalAndAppPriorYn(Approval app, String appPriorYn);


	@EntityGraph(attributePaths= {"accessor", "approval"})
	Optional<ApprovalLine> findByApprovalAndAccessorAndAppPriorYn(Approval app, Member mem, String appPriorYn);
	
    default ApprovalLine saveAndFlush(ApprovalLine approvalLine, ApprovalRepository approvalRepository) {
        Approval approval = approvalLine.getApproval();
        Approval savedApproval = approvalRepository.saveAndFlush(approval);
        approvalLine.setApproval(savedApproval);
        return save(approvalLine);
    }
}
