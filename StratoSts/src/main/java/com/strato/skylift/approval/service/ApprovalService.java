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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.strato.skylift.jwt.TokenProvider;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.dto.MbTokenDto;
import com.strato.skylift.member.exception.LoginFailedException;
import com.strato.skylift.member.dto.MbTokenDto;
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
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final ModelMapper mm;
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo,
						   AppMemberRepository appMbRepo,
						   MemberRepository mbRepo,
						   AppDeptRepository deptRepo,
						   AppJobRepository jobRepo,
						   PasswordEncoder passwordEncoder,
						   TokenProvider tokenProvider,
						   ModelMapper mm) {
		this.appRepo = appRepo;
		this.mbRepo = mbRepo;
		this.deptRepo = deptRepo;
		this.jobRepo = jobRepo;
		this.appLineRepo = appLineRepo;
		this.appMbRepo = appMbRepo;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
		this.mm = mm;
	}

	
	
/* 1. 결재문서 조회 - 결재 대기함 
   2. 결재문서 조회 - 결재 진행함
   3. 결재문서 조회 - 결재 완료함
   4. 결재문서 조회 - 결재 반려함 */
	// 상태별 결재문서 목록 조회
	public Page<ApprovalDto> selectApprovalList(int page, Long memberCode, String appStatus) {
		log.info("[ApprovalService] selectApprovalList start ============================== ");
		log.info("[ApprovalService] appStatus : {}", appStatus);
		
		Member member = appMbRepo.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("직원코드를 다시 확인해주세요 :  " + memberCode));
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("appCode").descending());
		
		Page<Approval> approvalList = appRepo.findByMemberCodeAndAppStatus(pageable, member.getMemberCode(), appStatus);
		Page<ApprovalDto> approvalDtoList = approvalList.map(approval -> mm.map(approval, ApprovalDto.class));
		log.info("[ApprovalService] approvalList : {}", approvalList);
		log.info("[ApprovalService] approvalDtoList : {}", approvalDtoList);
		
		log.info("[ApprovalService] selectApprovalList end ============================== ");
		return approvalDtoList;	
	}
	
/* 5. 결재요청문서 조회 - 본인이 결재선으로 설정된 결재문서 목록 조회 (전결 여부가 "Y"이고, 최종승인일이 null인 경우에만 조회) */
	public Page<ApprovalLineDto> getDemandList(int page, Long memberCode) {
		log.info("[ApprovalService] selectApprovalList start ============================== ");
		log.info("[ApprovalService] memberCode : {}", memberCode);
		Member member = appMbRepo.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("직원코드를 다시 확인해주세요 :  " + memberCode));
		log.info("[ApprovalService] member : {}", member);
		
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("appLineCode").ascending());
		Page<ApprovalLine> appLineList = appLineRepo.findByAccessorAndAppPriorYnAndAppTime(pageable, member, "Y", null);
		Page<ApprovalLineDto> apprLineDtoList = appLineList.map(appLine -> mm.map(appLine, ApprovalLineDto.class));
		log.info("[ApprovalService] approvalList : {}", appLineList);
		log.info("[ApprovalService] approvalDtoList : {}", apprLineDtoList);
		
		log.info("[ApprovalService] selectApprovalList end ============================== ");
		return apprLineDtoList;	
	}
	
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
		log.info("[ApprovalService] getMemberInfoForApproval start ============================== ");
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
	public MbMemberDto getSelectedMemberInfo(Long memberCode) {
		Member member = mbRepo.findById(memberCode)
				.orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));
		log.info("[ApprovalService] member : {}" + member);
		MbMemberDto memberInfoDto = mm.map(member, MbMemberDto.class);
		return memberInfoDto;
	}

	// 결재문서 정보 조회 (결재코드 마지막에서 첫번째거 찾기 흠....)
	public ApprovalDto getApprovalInfo() {
		Approval approval = appRepo.findFirstByOrderByAppRegistDateDesc()
				.orElseThrow(() -> new IllegalArgumentException("결재 문서가 없습니다."));
	    ApprovalDto approvalDto = mm.map(approval, ApprovalDto.class);
		return approvalDto;
	}
	
	//결재선 등록
	@Transactional
	public void insertAppLine(ApprovalLineDto appLineDto) {
		log.info("[ApprovalService] insertAppLine start---------------------------------------------------------------- ");
		// 가장 최근에 저장된 기안코드 불러오기
		Approval approval = appRepo.findFirstByOrderByAppRegistDateDesc().orElseThrow(()-> new IllegalArgumentException("결재문서 조회 실패"));
	    log.info("[ApprovalService] approval: {} ", approval);
	    ApprovalDto approvalDto = mm.map(approval, ApprovalDto.class);
	    log.info("[ApprovalService] approvalDto: {} ", approvalDto);
		
	    appLineDto.setApproval(approvalDto);
	    log.info("[ApprovalService] appLineDto: {} ", appLineDto);
	    
		//applneDto에 조회한 정보 넣고 저장
	    appLineRepo.save(mm.map(appLineDto, ApprovalLine.class));
	    
	    log.info("[ApprovalService] appLineRepo : {}" + appLineRepo);
	    log.info("[ApprovalService] insertAppLine end---------------------------------------------------------------- ");
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

	public List<ApprovalLineDto> selectAppLineDetail(Long appCode) {
		log.info("[ApprovalService] selectAppLineDetail start ============================== ");
		log.info("[ApprovalService] appCode : {}", appCode);
		Approval approval = appRepo.findById(appCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 결재 문서가 없습니다. appCode : " + appCode));
		log.info("[ApprovalService] approval : {}", approval);
		ApprovalDto approvalDto = mm.map(approval, ApprovalDto.class);
		log.info("[ApprovalService] approvalDto : {}", approvalDto);
		
		List<ApprovalLine> appLineList = appLineRepo.findAllByApproval(appCode);
		log.info("[ApprovalService] appLine : {}", appLineList);
		
		List<ApprovalLineDto> appLineListDto = appLineList.stream()
				.map(appLine -> mm.map(appLine, ApprovalLineDto.class)).collect(Collectors.toList());
		log.info("[ApprovalService] appLineDto : {}", appLineListDto);
		
		log.info("[ApprovalService] selectAppLineDetail end ============================== ");
		return appLineListDto;		
		
//		List<Department> deptList = deptRepo.findAll();
//		List<MbDepartmentDto> deptDtoList = deptList.stream()
//				.map(dept -> mm.map(dept, MbDepartmentDto.class))
//				.collect(Collectors.toList());
//		return deptDtoList;
	}
	


// 결재 승인/반려
	@Transactional
	public void putApprovalAccess(ApprovalLineDto appLineDto) {
		log.info("[ApprovalService] putApprovalAccess start ============================== ");
		log.info("[ApprovalService] appLineDto : {}", appLineDto);
		
		ApprovalLine orginAppLine = appLineRepo.findByAppOrderAndAppPriorYn(appLineDto.getAppOrder(), "Y")
				.orElseThrow(()-> new IllegalArgumentException("해당 결재는 아직 전결되지 않았습니다."));
		
		// 조회했던 기존 엔티티의 내용을 수정 -> 별조의 수정 메소드를 정의해서 사용하면 다른 방식의 수정을 막을 수 있다.
		orginAppLine.update(
				appLineDto.getAppPriorYn(),
				appLineDto.getAppLineStatus(),
				appLineDto.getAppTime()
		);
				
		
		
		log.info("[ApprovalService] putApprovalAccess end ============================== ");
	}



	public MbTokenDto identifyAccessor(MbMemberDto memberDto) {
		Member member = appMbRepo.findByMemberId(memberDto.getMemberId())
				.orElseThrow(() -> new LoginFailedException("회원아이디 조회 실패"));
		if(!passwordEncoder.matches(memberDto.getMemberPwd(), member.getMemberPwd())) {
			throw new LoginFailedException("비밀번호 번호가 틀렸습니다.");
		}
		// 3. 토큰 가져오기?? 흠.....
		MbTokenDto tokenDto = tokenProvider
				.generateTokenDto(mm.map(member, MbMemberDto.class));
		log.info("[AuthService] tokenDto : {}", tokenDto);
		
		log.info("[AuthService] login end ====================================================");
		
		return tokenDto;
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
