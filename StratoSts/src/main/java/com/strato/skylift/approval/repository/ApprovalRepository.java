package com.strato.skylift.approval.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.Member;


public interface ApprovalRepository extends JpaRepository<Approval, Long> {

	
/* 1. 결재문서 조회 - 결재 대기함
   2. 결재문서 조회 - 결재 진행함
   3. 결재문서 조회 - 결재 완료함
   4. 결재문서 조회 - 결재 반려함 */
//	@EntityGraph(attributePaths= {"member"})
//	Page<Approval> findByMemberAndAppStatus(Pageable pageable, String appStatus, Member findMember);
	
	//일단...	
	@EntityGraph(attributePaths= {"member"})
	Page<Approval> findByAppStatus(Pageable pageable, String appStatus);

	
	
/* 5. 결재문서 조회 - 본인이 결재해야 할 문서 목록 조회 */
/* 결재문서 상세 조회 - 작성자 */
	
/* 6. 기안문 작성  */
	
/* 7. 결재선, 열람인 선정 save 메소드로 정의함 */
	
/* 8. 결재 승인 
 *  결재문서 상세 조회 - 결재자 */
	
/* 9. 결재 반려 
 *  결재문서 상세 조회 - 결재자 */
	
	
}
