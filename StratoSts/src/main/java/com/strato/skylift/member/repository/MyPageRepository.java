package com.strato.skylift.member.repository;

import com.strato.skylift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MyPageRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberCode(@Param("memberCode") Long memberCode);


}
