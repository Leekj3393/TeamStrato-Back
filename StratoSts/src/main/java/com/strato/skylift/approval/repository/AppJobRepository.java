package com.strato.skylift.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Job;

public interface AppJobRepository extends JpaRepository<Job, Long> {

}
