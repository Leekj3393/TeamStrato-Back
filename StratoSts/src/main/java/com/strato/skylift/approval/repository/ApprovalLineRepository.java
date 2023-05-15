package com.strato.skylift.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.ApprovalLine;


public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>  {

}
