package com.strato.skylift.member.repository;

import com.strato.skylift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageRepository extends JpaRepository<Member, Long> {

    //1. 마이페이지 멤버 기본 정보 조회
    Member findByMemberCode(Long memberCode);
}
