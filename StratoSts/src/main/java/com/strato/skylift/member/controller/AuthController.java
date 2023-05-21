package com.strato.skylift.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.entity.Member;
import com.strato.skylift.mail.dto.MailDto;
import com.strato.skylift.mail.service.MailService;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.exception.LoginFailedException;
import com.strato.skylift.member.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final MailService mailService;
	
	public AuthController(AuthService authService, MailService mailService) {
		this.authService = authService;
		this.mailService = mailService;
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
	
	/* 직원 비밀번호 수정 */
	@PostMapping("/updatePwd")
	public ResponseEntity<ResponseDto> updateMemberPwdByMemberId(@RequestBody MbMemberDto memberDto) {
		
		Member member = authService.findByMemberIdAndmemberPwd(memberDto.getMemberId(), memberDto.getMemberPwd());
		
		authService.updateMemberPwdByMemberId(memberDto.getMemberId(), memberDto.getUpdatePwd());
		
		return ResponseEntity 
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"비밀번호 수정 성공"));
	}
	
    /* 비밀번호 찾기 이메일 보내기 */
    @PostMapping("/send")
    public ResponseEntity<ResponseDto> sendMail(@RequestBody MbMemberDto memberDto) {
    	Member member = authService.findMemberIdByResidentNo(memberDto.getResidentNo());
    	MailDto mail = mailService.createMailAndChangePassword(memberDto.getMemberId());
        mailService.mailSend(mail);
        
        return ResponseEntity 
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"임시비밀번호 발송 성공"));
    }
	
}