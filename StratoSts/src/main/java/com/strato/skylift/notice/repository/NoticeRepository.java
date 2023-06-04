package com.strato.skylift.notice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strato.skylift.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>
{
//유정--------------------------------------------------------------------------------------------------------------------------------------------
    Page<Notice> findByDepartmentDeptCode(String deptCode, Pageable pageable);

    List<Notice> findByNoticeTitleContaining(String keyword);

    @Query("SELECT n FROM Notice n "
            + "JOIN n.department d "
            + "WHERE d.deptCode = :deptCode AND n.noticeTitle LIKE %:noticeTitle%")
    Page<Notice> findByDeptCodeAndNoticeTitle(@Param("deptCode") String deptCode, @Param("noticeTitle") String noticeTitle, Pageable pageable);
//여기까지 유정 --------------------------------------------------------------------------------------------------------------------------------------
    

/* 1. 공지사항 전체 목록 조회 
   A. 관리자 전체 목록 조회
   B. 관리자 삭제된 공지사항 목록조회 */    
    @EntityGraph(attributePaths= {"department", "member"})
    Page<Notice> findByNoticeDelYn(Pageable pageable, String noticeDelYn);

/* 검색 - 제목 */
    @EntityGraph(attributePaths= {"department", "member"})
	Page<Notice> findByNoticeTitleContainsAndNoticeDelYn(Pageable pageable, String noticeTitle, String noticeDelYn);

/* 검색 - 내용 */
    @EntityGraph(attributePaths= {"department", "member"})
    Page<Notice> findByNoticeContentContainsAndNoticeDelYn(Pageable pageable, String noticeTitle, String noticeDelYn);

    List<Notice> findByNoticeCodeIn(List<Long> c);


    /* D. 관리자 공지 등록 */
/* E. 관리자 공지 수정 */
/* F. 관리자 공지 삭제 */

	
	
	
}
