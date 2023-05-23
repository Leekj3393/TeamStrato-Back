package com.strato.skylift.notice.repository;

import com.strato.skylift.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticePartRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByNoticeCode(Long noticeCode);

    Page<Notice> findByDepartment_DeptCodeAndNoticeTitleContaining(String deptCode, String noticeTitle, Pageable pageable);



    // Page<Notice> findByDeptCode(String department, Pageable pageable);
}

