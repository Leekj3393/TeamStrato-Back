package com.strato.skylift.approval.service;

import org.springframework.stereotype.Service;

import com.strato.skylift.approval.repository.ApprovalLineRepository;
import com.strato.skylift.approval.repository.ApprovalRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {

	private final ApprovalRepository appRepo;
	private final ApprovalLineRepository appLineRepo;
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo) {
		this.appRepo = appRepo;
		this.appLineRepo = appLineRepo;
	}
	
	
/* 1. 결재문서 조회 - 결재 대기함 */
/* 2. 결재문서 조회 - 결재 진행함 */
/* 3. 결재문서 조회 - 결재 완료함 */
/* 4. 결재문서 조회 - 결재 반려함 */
/* 5. 결재문서 조회 - 상신 문서함(본인이 상신한 문서함) */
/* 6. 기안문 작성  */
/* 7. 결재선, 열람인 선정  */
/* 8. 결재 승인  */
/* 9. 결재 반려  */
	
}
