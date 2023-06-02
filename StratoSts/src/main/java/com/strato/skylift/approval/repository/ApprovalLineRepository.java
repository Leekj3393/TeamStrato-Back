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

	@EntityGraph(attributePaths= {"accessor", "approval"})
	Page<ApprovalLine> findByAccessorAndAppPriorYnAndAppTime(Pageable pageable, Member accessor, String appPriorYn,
			Date appTime);


	@EntityGraph(attributePaths= {"accessor", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.approval.appCode = :appCode")
	List<ApprovalLine> findAllByApproval(Long appCode);

	
	@EntityGraph(attributePaths= {"accessor", "approval"})
	@Query("SELECT a FROM ApprovalLine a WHERE a.appOrder = :appOrder AND a.appPriorYn = :appPriorYn")
	Optional<ApprovalLine> findByAppOrderAndAppPriorYn(Long appOrder, String appPriorYn);

}
