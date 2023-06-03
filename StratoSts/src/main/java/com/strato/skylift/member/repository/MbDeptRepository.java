package com.strato.skylift.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Department;
import org.springframework.data.jpa.repository.Query;

public interface MbDeptRepository extends JpaRepository <Department, String>
{
    @Query(value = "SELECT d FROM Department d WHERE d.deptName = :noticeType")
    Department findByDeptName(String noticeType);
}
