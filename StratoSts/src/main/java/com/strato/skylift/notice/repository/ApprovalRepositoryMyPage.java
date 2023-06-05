package com.strato.skylift.notice.repository;

import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

@Repository
public interface ApprovalRepositoryMyPage extends JpaRepository<Approval, Long> {

    //리퀘스트 -> 어프로발에서 멤버에 멤버 코드 이건 머지된거
    @Query("SELECT a.request FROM Approval a WHERE a.member.memberCode = :memberCode")
    List<Request> findAllRequestsByMemberCode(@Param("memberCode") Long memberCode);


    Optional<Approval> findByRequest(Request request);
}

