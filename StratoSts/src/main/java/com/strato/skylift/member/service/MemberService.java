package com.strato.skylift.member.service;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Job;
import com.strato.skylift.entity.MbFile;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbFileDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.dto.MbMemberRoleDto;
import com.strato.skylift.member.repository.MbDeptRepository;
import com.strato.skylift.member.repository.MbFileRepository;
import com.strato.skylift.member.repository.MbJobRepository;
import com.strato.skylift.member.repository.MemberRepository;
import com.strato.skylift.member.util.MbFileUploadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final MbJobRepository jobRepository;
	private final MbDeptRepository deptRepository;	
	private final MbFileRepository fileRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	
	@Value("${image.image-url}")
	private String IMAGE_URL;
	
	@Value("${image.image-dir}")
	private String IMAGE_DIR;
	
	public MemberService(MemberRepository memberRepository, ModelMapper modelMapper, 
			MbJobRepository jobRepository, MbDeptRepository deptRepository, MbFileRepository fileRepository,
			PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.jobRepository = jobRepository;
		this.deptRepository = deptRepository;
		this.fileRepository = fileRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}
	
	/* 직원 전체목록 조회(공통) */
	public Page<MbMemberDto> selectMemberList(int page) {
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("memberCode").ascending());
		
		Page<Member> memberList = memberRepository.findAll(pageable);
		Page<MbMemberDto> memberDtoList = memberList.map(member -> modelMapper.map(member, MbMemberDto.class));
		
		return memberDtoList;
	}
	
	/* 직원 상세조회 */
	public MbMemberDto selectMemberDetail(Long memberCode) {
		
		Member member = memberRepository.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberCode));
		
		MbMemberDto memberDto = modelMapper.map(member, MbMemberDto.class);
		
		return memberDto;
	}
	
	/* 직원 사진조회 */
	public MbFileDto selectMemberImage(Long memberCode) {
		
		MbFile memberImage = fileRepository.findByMemberCode(memberCode);
		
		MbFileDto memberImageDto = modelMapper.map(memberImage, MbFileDto.class);
		
		return memberImageDto;
	}

	/* 직원 등록 */
	@Transactional
	public void insertMember(MbMemberDto memberDto) {
		
		log.info("memberDto : {}", memberDto);
		
		String imageName = UUID.randomUUID().toString().replace("-", "");
		
		MbFileDto fileDto = new MbFileDto();
		
		try {
			String replaceFilename = MbFileUploadUtils.saveFile(IMAGE_DIR + "/member", imageName, memberDto.getMemberImage());
			
			fileDto.setFileName(imageName);
			fileDto.setFilePath(replaceFilename);
			fileDto.setFileType("직원사진");
			
			/* 직원 등록시 기본값으로 권한코드 5번 부여 */
			MbMemberRoleDto memberRoleDto = new MbMemberRoleDto();
			memberRoleDto.setRoleCode((long) 5);
						
			memberDto.setMemberPwd(passwordEncoder.encode(memberDto.getMemberPwd()));
			memberDto.setMemberRole(memberRoleDto);
			memberDto.setMemberStatus("재직");
			
			Member newMember = memberRepository.save(modelMapper.map(memberDto, Member.class));
			
			fileDto.setMemberCode(newMember.getMemberCode());
				
			fileRepository.save(modelMapper.map(fileDto, MbFile.class));
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/* 직원 수정 */
	@Transactional
	public void updateMember(MbMemberDto memberDto) {
		
		Member originMember = memberRepository.findById(memberDto.getMemberCode())
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberDto.getMemberCode() ));
		
		try {
			/* 이미지를 변경하는 경우 */
			if(memberDto.getMemberImage() != null) {
			
				/* 새로 입력 된 이미지 저장 */
				String imageName = UUID.randomUUID().toString().replace("-", "");
				String replaceFileName = MbFileUploadUtils.saveFile(IMAGE_DIR + "/member", imageName, memberDto.getMemberImage());
				
				
				/* 기존에 저장 된 이미지 삭제 */
				MbFile memberFile = fileRepository.findByMemberCode(memberDto.getMemberCode());
				
				MbFileUploadUtils.deleteFile(IMAGE_DIR, memberFile.getFilePath());
				
				/* DB에 저장될 imageUrl 값을 수정 */
				memberFile.setFilePath(replaceFileName);
			} 
		
			/* 이미지를 변경하지 않는 경우에는 별도의 처리가 필요 없음 */
			
			/* 조회했던 기존 엔티티의 내용을 수정 -> 별도의 수정 메소드를 정의해서 사용하면 다른 방식의 수정을 막을 수 있다. */
				originMember.update(
						memberDto.getMemberName(),
						memberDto.getResidentNo(),
						memberDto.getGender(),
						memberDto.getPhone(),
						memberDto.getAddress(),
						memberDto.getBankName(),
						memberDto.getBankNo(),
						memberDto.getMemberSalary(),
						memberDto.getMemberAnnual()
						);
			
		}	catch (IOException e) {	
			e.printStackTrace();
		}
			
			
			
	}
		
		
	
	/* 직원 아이디 검색 */
	public Page<MbMemberDto> selectProductListByProductId(int page, String memberId) {
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("productCode").descending());
		
		Page<Member> memberList = memberRepository.findByMemberId(pageable, memberId);
				
		Page<MbMemberDto> memberDtoList = memberList.map(member -> modelMapper.map(member, MbMemberDto.class));
		
		return memberDtoList;
	}
	
	/* 직원 이름 검색 */
	public Page<MbMemberDto> selectProductListByProductName(int page, String memberName) {
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("productCode").descending());
		
		Page<Member> memberList = memberRepository.findByMemberName(pageable, memberName);
				
		Page<MbMemberDto> memberDtoList = memberList.map(member -> modelMapper.map(member, MbMemberDto.class));
		
		return memberDtoList;
	}
	
	/* 직급 조회 */
	public List<MbJobDto> selectJobList() {
		
		List<Job> jobList = jobRepository.findAll();
		
		List<MbJobDto> jobDtoList = jobList.stream()
	            .map(job -> modelMapper.map(job, MbJobDto.class))
	            .collect(Collectors.toList());
		
		return jobDtoList;
	}
	
	/* 부서 조회 */
	public List<MbDepartmentDto> selectDeptList() {
		
		List<Department> deptList = deptRepository.findAll();
		
		List<MbDepartmentDto> deptDtoList = deptList.stream()
				.map(dept -> modelMapper.map(dept, MbDepartmentDto.class))
				.collect(Collectors.toList());
		
		return deptDtoList;
	}
	
	
}
