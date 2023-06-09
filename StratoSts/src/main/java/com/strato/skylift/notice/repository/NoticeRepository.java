package com.strato.skylift.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

/* 1. 공지사항 전체 목록 조회  findAll 메소드 - 완료!! */
/* A. 관리자 전체 목록 조회 */
	
/* 2. 공지사항 부서별 목록 조회 - 미완성 */
	Page<Notice> findByDepartment(Pageable pageable, Department department);
	
/* 3. 공지사항 상태별 목록 조회 -  */
	Page<Notice> findByNoticeStatus(Pageable pageable, String noticeStatus);
	
/* B. 관리자 부서별 조회 */
/* C. 관리자 상태별 조회 */
/* D. 관리자 공지 등록 */
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	
	
	
}
