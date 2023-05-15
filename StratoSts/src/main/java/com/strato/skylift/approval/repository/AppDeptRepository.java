package com.strato.skylift.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Department;

public interface AppDeptRepository extends JpaRepository<Department, Long> {
	

}
