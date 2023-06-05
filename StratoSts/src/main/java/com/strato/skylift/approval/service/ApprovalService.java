package com.strato.skylift.approval.service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
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
	private final EntityManager entityManager;
	
	
	public ApprovalService(ApprovalRepository appRepo, 
						   ApprovalLineRepository appLineRepo,
						   AppMemberRepository appMbRepo,
						   MemberRepository mbRepo,
						   AppDeptRepository deptRepo,
						   AppJobRepository jobRepo,
						   PasswordEncoder passwordEncoder,
						   TokenProvider tokenProvider,
						   ModelMapper mm,
						   EntityManager entityManager) {
		this.appRepo = appRepo;
		this.mbRepo = mbRepo;
		this.deptRepo = deptRepo;
		this.jobRepo = jobRepo;
		this.appLineRepo = appLineRepo;
		this.appMbRepo = appMbRepo;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
		this.mm = mm;
		this.entityManager = entityManager;
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
				.map(appLine -> mm.map(appLine, ApprovalLineDto.class))
				.sorted(Comparator.comparing(ApprovalLineDto::getAppOrder)) // 여기에 정렬 로직 추가
				.collect(Collectors.toList());
		log.info("[ApprovalService] appLineDto : {}", appLineListDto);
		
		log.info("[ApprovalService] selectAppLineDetail end ============================== ");
		
		return appLineListDto;		
	}


// 결재 승인 시 결재문서 정보 업데이트
	@Transactional
	public void updateApproval(MbMemberDto memberDto, Long memberCode, Long appCode) {
		Approval findApproval = appRepo.findById(appCode).orElseThrow(()->new IllegalArgumentException("해당 코드의 결재문서가 존재하지 않습니다."));
		ApprovalDto approvalDto = mm.map(findApproval, ApprovalDto.class);
		
		ApprovalLine findAppLine = appLineRepo.findbyAccessorAndApproval(memberCode, appCode);
		ApprovalLineDto appLineDto = mm.map(findAppLine, ApprovalLineDto.class);
		
		
		appLineDto.setAppLineStatus("appAccessed");
		appLineDto.setAppTime(new Date());
		
		if(appLineDto.getAppOrder() != 3L) {
			approvalDto.setAppStatus("inProgress");
		} else {
			approvalDto.setAppStatus("accessed");
			approvalDto.setApprovedDate(new Date());
		}
		
		findApproval.update(
				approvalDto.getAppStatus(),
				approvalDto.getApprovedDate()
		);
		
		findAppLine.update(
				appLineDto.getAppPriorYn(),
				appLineDto.getAppLineStatus(),
				appLineDto.getAppTime()
		);
		
		//다음 결재선 정보 업데이트
		Long nextOrder = findAppLine.getAppOrder() + 1L;
		if(nextOrder > 1L && nextOrder <= 3L) {
			ApprovalLine nextAppLine = appLineRepo.findByApprovalAndAppOrder(appCode, nextOrder);
			ApprovalLineDto nextAppLineDto = mm.map(nextAppLine, ApprovalLineDto.class);
			nextAppLineDto.setAppPriorYn("Y");
			
			nextAppLine.update(
					nextAppLineDto.getAppPriorYn(),
					nextAppLineDto.getAppLineStatus(),
					nextAppLineDto.getAppTime()
			);
		} else {
			log.info("음?");
		}
	}



// 결재 반려 시
	@Transactional
	public void updateApprovalReturn(MbMemberDto memberDto, Long memberCode, Long appCode) {
		Approval findApproval = appRepo.findById(appCode).orElseThrow(()->new IllegalArgumentException("해당 코드의 결재문서가 존재하지 않습니다."));
		ApprovalDto approvalDto = mm.map(findApproval, ApprovalDto.class);
		
		ApprovalLine findAppLine = appLineRepo.findbyAccessorAndApproval(memberCode, appCode);
		ApprovalLineDto appLineDto = mm.map(findAppLine, ApprovalLineDto.class);
		
		
		appLineDto.setAppLineStatus("appReturned");
		appLineDto.setAppTime(new Date());
		
		approvalDto.setAppStatus("returned");
		approvalDto.setApprovedDate(new Date());
		
		findApproval.update(
				approvalDto.getAppStatus(),
				approvalDto.getApprovedDate()
		);
		
		findAppLine.update(
				appLineDto.getAppPriorYn(),
				appLineDto.getAppLineStatus(),
				appLineDto.getAppTime()
		);
		
		//다음 결재선 정보 업데이트
		Long nextOrder = findAppLine.getAppOrder() + 1L;
		if(nextOrder <= 3L && nextOrder > 1L) {
			ApprovalLine nextAppLine = appLineRepo.findByApprovalAndAppOrder(appCode, nextOrder);
			ApprovalLineDto nextAppLineDto = mm.map(nextAppLine, ApprovalLineDto.class);
			nextAppLineDto.setAppPriorYn("Y");
			nextAppLineDto.setAppLineStatus("appReturned");
			nextAppLineDto.setAppTime(new Date());
			
			nextAppLine.update(
					nextAppLineDto.getAppPriorYn(),
					nextAppLineDto.getAppLineStatus(),
					nextAppLineDto.getAppTime()
			);
			
			if(nextOrder == 2L) {
				ApprovalLine finalAppLine = appLineRepo.findByApprovalAndAppOrder(appCode, 3L);
				ApprovalLineDto finalAppLineDto = mm.map(finalAppLine, ApprovalLineDto.class);
				finalAppLineDto.setAppPriorYn("Y");
				finalAppLineDto.setAppLineStatus("appReturned");
				finalAppLineDto.setAppTime(new Date());
				
				finalAppLine.update(
						finalAppLineDto.getAppPriorYn(),
						finalAppLineDto.getAppLineStatus(),
						finalAppLineDto.getAppTime()
						);
				
			}
		} else {
			log.info("음?");
		}
	}



	public ApprovalLineDto getAppLineCode(MbMemberDto memberDto, Long memberCode, Long appCode) {
		Approval findApproval = appRepo.findById(appCode).orElseThrow(()->new IllegalArgumentException("해당 코드의 결재문서가 존재하지 않습니다."));

		Member member= mm.map(memberDto, Member.class);
		appCode = findApproval.getAppCode();
		memberCode = member.getMemberCode();
	
		
		ApprovalLine findAppLine = appLineRepo.findbyAccessorAndApproval(memberCode, appCode);
		ApprovalLineDto appLineDto = mm.map(findAppLine, ApprovalLineDto.class);
		
		log.info("appLineDto : {}", appLineDto);
		return appLineDto;
	}



	public List<ApprovalDto> countApprovalList(Long memberCode, String appStatus) {
		log.info("[ApprovalService] selectApprovalList start ============================== ");
		log.info("[ApprovalService] appStatus : {}", appStatus);
		
		Member member = appMbRepo.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("직원코드를 다시 확인해주세요 :  " + memberCode));
		
		
		List<ApprovalDto> approvalDtoList = appRepo.findAllByMemberAndAppStatus(member, appStatus,  Sort.by("appCode").descending())
				.stream().map(approval -> mm.map(approval, ApprovalDto.class)).collect(Collectors.toList());
		log.info("[ApprovalService] approvalDtoList : {}", approvalDtoList);
		
		log.info("[ApprovalService] selectApprovalList end ============================== ");
		return approvalDtoList;	
	}

}