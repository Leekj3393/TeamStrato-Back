package com.strato.skylift.member.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;
	
	public MemberService(MemberRepository memberRepository, ModelMapper modelMapper) {
		this.memberRepository = memberRepository;
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

	/* 직원 등록 */
	@Transactional
	public void insertMember(MbMemberDto memberDto) {
		
		memberRepository.save(modelMapper.map(memberDto, Member.class));
		
	}

	/* 직원 수정 */
	@Transactional
	public void updateMember(MbMemberDto memberDto) {
		
		Member originMember = memberRepository.findById(memberDto.getMemberCode())
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 직원이 없습니다. memberCode : " + memberDto.getMemberCode() ));
		
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
	
	
}
