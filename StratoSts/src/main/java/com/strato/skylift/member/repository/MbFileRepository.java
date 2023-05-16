package com.strato.skylift.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.MbFile;

public interface MbFileRepository extends JpaRepository <MbFile, Long> {

}
