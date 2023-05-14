package com.strato.skylift.notice.repository;

import com.strato.skylift.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticePartRepository extends JpaRepository<Notice, Long> {
   // Page<Notice> findByDeptCode(String department, Pageable pageable);
}

