package com.strato.skylift.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.strato.skylift.entity.NoticeFile;

import java.util.List;
import java.util.Optional;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {

//	@Query("SELECT f " +
//			   "  FROM NoticeFile f " +
//			   " WHERE f.noticeCode = :noticeCode "
//				)
//		NoticeFile findByNoticeCode(Long noticeCode);


	//유정
	List<NoticeFile> findByNoticeCode(Long noticeCode);

	NoticeFile findByFileName(String fileName);
}
