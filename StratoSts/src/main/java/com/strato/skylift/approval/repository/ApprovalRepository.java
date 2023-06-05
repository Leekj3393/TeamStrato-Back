package com.strato.skylift.approval.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

	
/* 1. 결재문서 조회 - 결재 대기함
   2. 결재문서 조회 - 결재 진행함
   3. 결재문서 조회 - 결재 완료함
   4. 결재문서 조회 - 결재 반려함 */
//	@EntityGraph(attributePaths= {"member"})
//	Page<Approval> findByMemberAndAppStatus(Pageable pageable, String appStatus, Member findMember);
	//
	
	@Query("SELECT a FROM Approval a WHERE a.member.memberCode = :memberCode AND a.appStatus = :appStatus")
	Page<Approval> findByMemberCodeAndAppStatus(Pageable pageable, Long memberCode, String appStatus);


	//

	
	
/* 5. 결재문서 조회 - 본인이 결재해야 할 문서 목록 조회 */
/* 결재문서 상세 조회 - 작성자 */
	
/* 6. 기안문 작성 -save() */


	
/* 7. 결재선 선정  */
	// 7-1. 결재 문서 조회
	@EntityGraph(attributePaths= {"member"})
	Optional<Approval> findFirstByOrderByAppRegistDateDesc();






	
/* 8. 결재 승인 
 *  결재문서 상세 조회 - 결재자 */
	
/* 9. 결재 반려 
 *  결재문서 상세 조회 - 결재자 */
	
    @SuppressWarnings("unchecked")
	default Approval saveAndFlush(Approval approval) {
        Approval savedApproval = save(approval);
        flush();
        return savedApproval;
    }


    @Query("SELECT a FROM Approval a WHERE a.member.memberCode = :memberCode AND a.appStatus = :appStatus")
	List<Approval> findByMemberCodeAndAppStatus(Long memberCode, String appStatus);

    @EntityGraph(attributePaths = {"approval", "approval.member"})
	List<Approval> findAllByMemberAndAppStatus(Member member, String appStatus, Sort descending);
}
