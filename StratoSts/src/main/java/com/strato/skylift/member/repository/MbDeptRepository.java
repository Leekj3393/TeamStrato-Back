package com.strato.skylift.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Department;

public interface MbDeptRepository extends JpaRepository <Department, String> {

}
