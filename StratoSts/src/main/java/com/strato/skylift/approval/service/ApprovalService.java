package com.strato.skylift.approval.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.approval.dto.ApprovalLineDto;
import com.strato.skylift.approval.repository.AppDeptRepository;
import com.strato.skylift.approval.repository.AppJobRepository;
import com.strato.skylift.approval.repository.AppMemberRepository;
import com.strato.skylift.approval.repository.ApprovalLineRepository;
import com.strato.skylift.approval.repository.ApprovalRepository;
import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.ApprovalLine;
import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Job;
import com.strato.skylift.entity.Member;
import com.strato.skylift.exception.UserNotFoundException;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService {

	private final ApprovalRepository appRepo;
	private final MemberRepository mbRepo;
	private final AppMemberRepository appMbRepo;
	private final AppDeptRepository deptRepo;
	private final AppJobRepository jobRepo;
	private final ApprovalLineRepository appLineRepo;
	private final ModelMapper mm;
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo,
						   AppMemberRepository appMbRepo,
						   MemberRepository mbRepo,
						   AppDeptRepository deptRepo,
						   AppJobRepository jobRepo,
						   ModelMapper mm) {
		this.appRepo = appRepo;
		this.mbRepo = mbRepo;
		this.deptRepo = deptRepo;
		this.jobRepo = jobRepo;
		this.appLineRepo = appLineRepo;
		this.appMbRepo = appMbRepo;
		this.mm = mm;
	}

	
	
/* 1. 결재문서 조회 - 결재 대기함 
   2. 결재문서 조회 - 결재 진행함
   3. 결재문서 조회 - 결재 완료함
   4. 결재문서 조회 - 결재 반려함 */
	// 테스트 중
	// 상태별 결재문서 목록 조회
	public Page<ApprovalDto> selectWaitingList(int page, String appStatus) {
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("appCode").ascending());
		
		
		
		//직원 정보 조회
		
//		Member findMember = mbRepo.findById(memberCode)
//			.orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. memberCode = "+ memberCode));
//		log.info("findMember : {}"+ findMember);
		
		Page<Approval> approvalList = appRepo.findByAppStatus(pageable, appStatus);
		Page<ApprovalDto> approvalDtoList = approvalList.map(approval -> mm.map(approval, ApprovalDto.class));
		
		return approvalDtoList;
	}
	
	
/* 5. 결재문서 조회 - 상신 문서함(본인이 상신한 문서함) */
/* 6. 기안문 작성  */
	// 직원 상세 조회
	public MbMemberDto selectMemberDetailForApproval(Long memberCode) {
		Member member = mbRepo.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberCode));
		
		MbMemberDto memberDto = mm.map(member, MbMemberDto.class);
		
		return memberDto;
	}
	// 로그인한 직원 조회
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
	
	// 기안문 등록
	@Transactional
	public void registApp(ApprovalDto appDto) {
		appRepo.save(mm.map(appDto, Approval.class));
	}
	

	
/* 7. 결재선 선정  */
	@Transactional
	public void insertAppLine1(Long memberCode, Long appCode) {
		Optional<Member> memberOptional = appMbRepo.findById(memberCode);
		Optional<Approval> approvalOptional = appRepo.findById(appCode);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Approval approval = approvalOptional.get();
            
            ApprovalLine newAppline1 = new ApprovalLine();
            newAppline1.setMember(member);
            newAppline1.setAppPriorYn("Y");
            newAppline1.setApproval(approval);
            newAppline1.setAppStatus("결재 대기");
            newAppline1.setAppOrder(1L);
            appLineRepo.save(newAppline1);
        }
	}
	@Transactional
	public void insertAppLine2(Long memberCode, Long appCode) {
		Optional<Member> memberOptional = appMbRepo.findById(memberCode);
		Optional<Approval> approvalOptional = appRepo.findById(appCode);
		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			Approval approval = approvalOptional.get();
			
			ApprovalLine newAppline2 = new ApprovalLine();
			newAppline2.setMember(member);
			newAppline2.setAppPriorYn("N");
			newAppline2.setApproval(approval);
			newAppline2.setAppStatus("결재 대기");
			newAppline2.setAppOrder(2L);
			appLineRepo.save(newAppline2);
		}
	}
	
	@Transactional
	public void insertAppLine3(Long memberCode, Long appCode) {
		Optional<Member> memberOptional = appMbRepo.findById(memberCode);
		Optional<Approval> approvalOptional = appRepo.findById(appCode);
		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			Approval approval = approvalOptional.get();
			
			ApprovalLine newAppline3 = new ApprovalLine();
			newAppline3.setMember(member);
			newAppline3.setAppPriorYn("N");
			newAppline3.setApproval(approval);
			newAppline3.setAppStatus("결재 대기");
			newAppline3.setAppOrder(3L);
			appLineRepo.save(newAppline3);
		}
	}

	// 직원 전체 조회 >> 부서순 정렬되도록!~!!!
	public List<MbMemberDto> selectMemberList() {
		List<Member> memberList = mbRepo.findAll();
		
		List<MbMemberDto> memberDtoList = memberList.stream()
				.map(accessor -> mm.map(accessor, MbMemberDto.class))
				.collect(Collectors.toList());
		
		return memberDtoList;
	}

	//부서 조회
	public List<MbDepartmentDto> selectDeptList() {
		List<Department> deptList = deptRepo.findAll();
		
		List<MbDepartmentDto> deptDtoList = deptList.stream()
				.map(dept -> mm.map(dept, MbDepartmentDto.class))
				.collect(Collectors.toList());
		
		return deptDtoList;
	}

	// 직급 조회
	public List<MbJobDto> selectJobList() {
		List<Job> jobList = jobRepo.findAll();
		
		List<MbJobDto> jobDtoList = jobList.stream()
				.map(job -> mm.map(job, MbJobDto.class))
				.collect(Collectors.toList());
		
		return jobDtoList;
	}


	// 결재선으로 선택된 직원의 정보를 조회함
	public Optional<Member> getSelectedMemberInfo(Long memberCode) {
		Member member = mbRepo.findById(memberCode)
				.orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
		
		Optional<Member> memberInfo = mbRepo.findById(memberCode);
		
		return memberInfo;
	}
	
	
// 10. 결재문서 상세 조회
	public ApprovalDto selectApprovalDetail(Long appCode) {
		log.info("[ApprovalService] selectApprovalDetail start ============================== ");
		log.info("[ApprovalService] appCode : {}", appCode);
		
		Approval approval = appRepo.findById(appCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 결재 문서가 없습니다. appCode : " + appCode));
		log.info("[ApprovalService] approval : {}", approval);
		
		ApprovalDto approvalDto = mm.map(approval, ApprovalDto.class);
		log.info("[ApprovalService] approvalDto : {}", approvalDto);
		
		log.info("[ApprovalService] selectApprovalDetail end ============================== ");
		return approvalDto;		
	}


// 결재 승인/반려
	@Transactional
	public void putApprovalAccess(ApprovalLineDto appLineDto) {
		log.info("[ApprovalService] putApprovalAccess start ============================== ");
		log.info("[ApprovalService] appLineDto : {}", appLineDto);
		
		ApprovalLine beforeAppLine = appLineRepo.findById(appLineDto.getAppLineCode())
				.orElseThrow(()-> new IllegalArgumentException("해당 코드의 결재선 정보가 없습니다. appLineCode=" + appLineDto.getAppLineCode()));
		
		// 조회했던 기존 엔티티의 내용을 수정 -> 별조의 수정 메소드를 정의해서 사용하면 다른 방식의 수정을 막을 수 있다.
		beforeAppLine.update(
				appLineDto.getAppPriorYn(),
				appLineDto.getAppStatus(),
				appLineDto.getAppTime()
		);
				
		
		
		log.info("[ApprovalService] putApprovalAccess end ============================== ");
	}



	public ApprovalDto getApprovalInfo(Long appCode) {
		
		Approval approval = appRepo.findById(appCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 결재 문서가 없습니다. appCode : " + appCode));
		
		ApprovalDto approvalDto = mm.map(approval, ApprovalDto.class);
		
		return approvalDto;
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
