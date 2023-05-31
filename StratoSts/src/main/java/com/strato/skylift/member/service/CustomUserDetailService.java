package com.strato.skylift.member.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.strato.skylift.member.dto.MbFileDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.strato.skylift.exception.UserNotFoundException;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;
	
	public CustomUserDetailService(ModelMapper modelMapper, MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		log.info("[CustomUserDetailService] loadUserByUsername start =========================");
		log.info("[CustomUserDetailService] userId : {}", userId);
		
		return memberRepository.findByMemberId(userId)
				.map(user -> addAuthorities(user))
				.orElseThrow(() -> new UserNotFoundException(userId + "를 찾을 수 없습니다."));
	}
	
	/* Member entity를 MemberDto로 가공하면서 authorities의 값도 가공해서 반환하는 메소드 */
	private MbMemberDto addAuthorities(Member member) {
		
		MbMemberDto memberDto = modelMapper.map(member, MbMemberDto.class);
		memberDto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(memberDto.getMemberRole().getRoleName())));
		
		return memberDto;
	}

}
