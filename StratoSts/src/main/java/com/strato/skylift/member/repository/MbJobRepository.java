package com.strato.skylift.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Job;

public interface MbJobRepository extends JpaRepository <Job, String> {
	
}
