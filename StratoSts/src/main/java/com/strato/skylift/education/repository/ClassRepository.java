package com.strato.skylift.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.education.entity.EdClass;

public interface ClassRepository extends JpaRepository <EdClass, Long> {

}
