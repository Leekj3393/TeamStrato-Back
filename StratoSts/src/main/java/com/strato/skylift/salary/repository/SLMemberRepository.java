package com.strato.skylift.salary.repository;

import com.strato.skylift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SLMemberRepository extends JpaRepository<Member , Long>
{
    @Query(value = "SELECT m FROM Member m WHERE m.memberName LIKE %:value%")
    List<Member> findByMemberNameLike(String value);
}
