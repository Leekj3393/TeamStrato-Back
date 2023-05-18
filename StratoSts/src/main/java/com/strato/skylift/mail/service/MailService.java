package com.strato.skylift.mail.service;

import com.strato.skylift.mail.dto.MailDto;
import com.strato.skylift.member.dto.MbMemberDto;

public interface MailService {

    MailDto createMailAndChangePassword(String str);

    void mailSend(MailDto mail);

    String getTempPassword();

	void updateMemberPwd(String memberId, String pass);
}