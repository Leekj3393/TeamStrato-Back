package com.strato.skylift.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	/* 로그인 */
	@PostMapping("/login")
	public ResponseEntity<ResponseDto> login(@RequestBody MbMemberDto memberDto) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "로그인 완료", authService.login(memberDto)));
	}
	
	/* Id찾기 */
	@PostMapping("/findid")
	public ResponseEntity<ResponseDto> findid(@RequestBody MbMemberDto memberDto) {
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "입력하신 정보의 Id : ", authService.findMemberIdByMemberNameAndResidentNo(memberDto)));
	}
}