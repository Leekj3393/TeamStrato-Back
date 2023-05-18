package com.strato.skylift.mail.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.strato.skylift.entity.Member;
import com.strato.skylift.mail.dto.MailDto;
import com.strato.skylift.member.service.AuthService;
import com.strato.skylift.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSenderImpl mailSender;
    private final MemberService memberService;
    private final AuthService authService;

    public MailServiceImpl(JavaMailSenderImpl mailSender,
                           MemberService memberService, PasswordEncoder pe,
                           AuthService authService) {
        this.mailSender = mailSender;
        this.memberService = memberService;
        this.authService = authService;
    }


    @Override
    public MailDto createMailAndChangePassword(String memberId) {
        String pass = getTempPassword();
        MailDto mail = new MailDto();
        mail.setAddress(memberId);
        mail.setTitle(" SkyLift 임시비밀번호 안내 이메일 입니다.");
        mail.setMessage("안녕하세요. SkyLift 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + pass + "입니다." + "로그인 후에 비밀번호를 변경을 해주세요. 틀리지마세요 ㅋ");
        updateMemberPwd(memberId, pass);
        return mail;
    }

    @Override
    public void mailSend(MailDto mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());
        message.setFrom("lkj9676@naver.com");
        message.setReplyTo("lkj9676@naver.com");
        log.info("message : {} ", message);
        log.info("mailSender : {} ", mailSender);
        mailSender.send(message);
    }

    @Override
    public String getTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    @Override
    public void updateMemberPwd(String memberId, String pass) {
        Member member = authService.findByMemberId(memberId);
        authService.updateMemberPwdByMemberId(memberId, pass);
        
    }
}