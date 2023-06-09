package com.strato.skylift.member.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strato.skylift.jwt.TokenProvider;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.dto.MbTokenDto;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.exception.DuplicatedUserEmailRuntimeException;
import com.strato.skylift.member.exception.LoginFailedException;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final TokenProvider tokenProvider;
	
	public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
		this.tokenProvider = tokenProvider;
	}
	

	/* 2. 로그인 */
	public MbTokenDto login(MbMemberDto memberDto) {
		
		log.info("[AuthService] login start ===================================================");
		log.info("[AuthService] memberDto : {}", memberDto);
		
		// 1. 아이디로 DB에서 해당 유저가 있는지 조회
		Member member = memberRepository.findByMemberId(memberDto.getMemberId())
				.orElseThrow(() -> new LoginFailedException("잘못 된 아이디 또는 비밀번호입니다."));
		
		// 2. 비밀번호 매칭 확인 
		// 가데이터 사용할 때 사용 할 구문
		if((memberDto.getMemberPwd() == member.getMemberPwd())) {
			throw new LoginFailedException("잘못 된 아이디 또는 비밀번호입니다.");}
		// 솔팅 처리 후 사용 할 구문
//		if(!passwordEncoder.matches(memberDto.getMemberPwd(), member.getMemberPwd())) {
//			throw new LoginFailedException("잘못 된 아이디 또는 비밀번호입니다.");
//		}
		
		// 3. 토큰 발급
		MbTokenDto tokenDto = tokenProvider.generateTokenDto(modelMapper.map(member, MbMemberDto.class));
		log.info("[AuthService] tokenDto : {}", tokenDto);
		
		log.info("[AuthService] login end ====================================================");
		
		return tokenDto;
	}

}
