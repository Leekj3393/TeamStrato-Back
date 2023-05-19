package com.strato.skylift.approval.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.repository.AppDeptRepository;
import com.strato.skylift.approval.repository.AppJobRepository;
import com.strato.skylift.approval.repository.ApprovalLineRepository;
import com.strato.skylift.approval.repository.ApprovalRepository;
import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Member;
import com.strato.skylift.exception.UserNotFoundException;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {

	private final ApprovalRepository appRepo;
//	private final AppMemberRepository mbRepo;
	private final MemberRepository mbRepo;
	private final AppDeptRepository deptRepo;
	private final AppJobRepository jobRepo;
	private final ApprovalLineRepository appLineRepo;
	private final ModelMapper mm;
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo,
//						   AppMemberRepository mbRepo,
						   MemberRepository mbRepo,
						   AppDeptRepository deptRepo,
						   AppJobRepository jobRepo,
						   ModelMapper mm) {
		this.appRepo = appRepo;
		this.mbRepo = mbRepo;
		this.deptRepo = deptRepo;
		this.jobRepo = jobRepo;
		this.appLineRepo = appLineRepo;
		this.mm = mm;
	}

	
	
/* 1. 결재문서 조회 - 결재 대기함 */
/* 2. 결재문서 조회 - 결재 진행함 */
/* 3. 결재문서 조회 - 결재 완료함 */
/* 4. 결재문서 조회 - 결재 반려함 */
/* 5. 결재문서 조회 - 상신 문서함(본인이 상신한 문서함) */
/* 6. 기안문 작성  */
	// 직원 상세 조회
	public MbMemberDto selectMemberDetailForApproval(Long memberCode) {
		Member member = mbRepo.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberCode));
		
		MbMemberDto memberDto = mm.map(member, MbMemberDto.class);
		
		return memberDto;
	}
	
	// 기안문 등록
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


//	// 부서 조회
//	public List<MbDepartmentDto> selectDeptList() {
//		
//		List<Department> deptList = deptRepo.findAll();
//		
//		List<MbDepartmentDto> deptDtoList = deptList.stream()
//				.map(dept2 -> mm.map(dept2, MbDepartmentDto.class))
//				.collect(Collectors.toList());
//		
//		return deptDtoList;
//	}
//
//
//	// 직급 조회
//	public List<MbJobDto> selectJobList() {
//		List<Job> jobList = jobRepo.findAll();
//		
//		List<MbJobDto> jobDtoList = jobList.stream()
//				.map(job2 -> mm.map(job2, MbJobDto.class))
//				.collect(Collectors.toList());
//		
//		return jobDtoList;
//	}

	// 직원 전체 조회 >> 부서순 정렬되도록!~!!!
	public List<MbMemberDto> selectMemberList() {
		List<Member> memberList = mbRepo.findAll();
		
		List<MbMemberDto> MemberDtoList = memberList.stream()
				.map(appline -> mm.map(appline, MbMemberDto.class))
				.collect(Collectors.toList());
		
		return MemberDtoList;
	}



//	public MbMemberDto getMemberInfoByMemberCode(Long memberCode) {
//
//		// 로그인한 직원의 멤버코드를 기준으로 db에서 회원정보를 조회한다.
//		Member member = mbRepo.findById(memberCode).orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberCode));
//		MbMemberDto memberDto = mm.map(member, MbMemberDto.class);
//
//		// 조회된 직원 정보를 반환한다.
//		return memberDto;
//	}


	// 직원 조회
	public Optional<Member> getMemberInfoForApproval(Long memberCode) {
		log.info("[ApprovalService] selectPurchaseList start ============================== ");
		log.info("[ApprovalService] memberCode : {}", memberCode);
		
		Member member = mbRepo.findById(memberCode)
				.orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
		
		Optional<Member> memberInfo = mbRepo.findById(memberCode);
		
		log.info("[ApprovalService] purchaseList : {}", memberInfo);
		log.info("[ApprovalService] selectPurchaseList end ============================== ");
		return memberInfo;
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
