package com.strato.skylift.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

/* 1. 공지사항 전체 목록 조회  findAll 메소드 - 완료!! */
/* A. 관리자 전체 목록 조회 */

/* 검색 - 제목 */
/* 검색 - 내용 */
	
/* D. 관리자 공지 등록 */
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	
	
	
}
