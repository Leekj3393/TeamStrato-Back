package com.strato.skylift.notice.repository;

import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r LEFT JOIN FETCH r.approvals a LEFT JOIN FETCH a.member")
    List<Request> findAllWithApprovalsAndMembers();


}




