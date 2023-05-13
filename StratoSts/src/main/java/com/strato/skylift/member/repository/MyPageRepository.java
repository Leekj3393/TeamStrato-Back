package com.strato.skylift.member.repository;

import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MyPageRepository extends JpaRepository<Member, Long> {

    //1. 마이페이지 멤버 기본 정보 조회
    //@Query("SELECT m FROM Member m WHERE m.memberCode = :memberCode")
    Optional<Member> findByMemberCode(@Param("memberCode") Long memberCode);


}
