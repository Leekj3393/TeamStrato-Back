package com.strato.skylift.notice.repository;

import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r LEFT JOIN FETCH r.approvals a LEFT JOIN FETCH a.member")
    List<Request> findAllWithApprovalsAndMembers();

    List<Request> findAllByApprovals_Member(Member member);


//        Optional<Member> findByMemberCode(Long memberCode);

}




