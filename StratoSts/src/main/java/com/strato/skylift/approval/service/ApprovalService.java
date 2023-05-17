package com.strato.skylift.approval.service;


import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.repository.ApprovalLineRepository;
import com.strato.skylift.approval.repository.ApprovalRepository;
import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MbDeptRepository;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {

	private final ApprovalRepository appRepo;
	private final MemberRepository mbRepo;
	private final MbDeptRepository deptRepo;
	private final ApprovalLineRepository appLineRepo;
	private final ModelMapper mm;
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo,
						   MemberRepository mbRepo,
						   MbDeptRepository deptRepo,
						   ModelMapper mm) {
		this.appRepo = appRepo;
		this.mbRepo = mbRepo;
		this.deptRepo = deptRepo;
		this.appLineRepo = appLineRepo;
		this.mm = mm;
	}

	
	
/* 1. 결재문서 조회 - 결재 대기함 */
/* 2. 결재문서 조회 - 결재 진행함 */
/* 3. 결재문서 조회 - 결재 완료함 */
/* 4. 결재문서 조회 - 결재 반려함 */
/* 5. 결재문서 조회 - 상신 문서함(본인이 상신한 문서함) */
/* 6. 기안문 작성  */
	@Transactional
	public void registApp(ApprovalDto appDto) {
		appRepo.save(mm.map(appDto, Approval.class));
	}
	
	
/* 7. 결재선, 열람인 선정  */
	@Transactional
	public void insertAppLine(ApprovalLineDto applineDto) {
		log.info("[ApprovalService] insertAppLine start ===========================================");
		log.info("[ApprovalService] applineDto : {}", applineDto);
		
		appLineRepo.save(mm.map(applineDto, ApprovalLine.class));
		
		
		log.info("[ApprovalService] insertAppLine end ===========================================");
		
	}

	// 직원 전체 조회
	public List<MbMemberDto> selectMemberList() {
		List<Member> memberList = mbRepo.findAll();
		
		List<MbMemberDto> MemberDtoList = memberList.stream()
	            .map(member -> mm.map(member, MbMemberDto.class))
	            .collect(Collectors.toList());
		
		return MemberDtoList;
	}

	// 부서 조회
	public List<MbDepartmentDto> selectDeptList() {
		
		List<Department> deptList = deptRepo.findAll();
		
		List<MbDepartmentDto> deptDtoList = deptList.stream()
				.map(dept -> mm.map(dept, MbDepartmentDto.class))
				.collect(Collectors.toList());
		
		return deptDtoList;
	}












	
//	public List<MbMemberDto> getMemberList() {
//		
//		List<Member> memberList = mbRepo.findAll();
//		List<MbMemberDto> memberDtoList = memberList.stream()
//				.map(member -> mm.map(member, MbMemberDto.class))
//				.collect(Collectors.toList());
//		
//		return memberDtoList;
//	}
//
//
//
//	public List<MbDepartmentDto> getDeptList() {
//		List<Department> deptList = deptRepo.findAll();
//		List<MbDepartmentDto> deptDtoList = deptList.stream()
//				.map(dept -> mm.map(dept, MbDepartmentDto.class))
//				.collect(Collectors.toList());
//		
//		
//		return deptDtoList;
//	}
	
/* 8. 결재 승인  */
/* 9. 결재 반려  */
	
}
