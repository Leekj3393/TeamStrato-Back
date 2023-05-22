package com.strato.skylift.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Education;
import com.strato.skylift.entity.Member;

public interface EducationRepository extends JpaRepository <Education, Long> {

	Page<Education> findAll(Pageable pageable);
		
}
