package com.strato.skylift.salary.repository;

import com.strato.skylift.entity.SalaryStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaryRepository extends JpaRepository<SalaryStatement , Long>
{
    @Query(value = "SELECT s FROM SalaryStatement s WHERE s.member.memberCode = :memberCode ORDER BY s.salaryCode")
    Page<SalaryStatement> findByMemberMemberId(Long memberCode , Pageable pageable);

}
