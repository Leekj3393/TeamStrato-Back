package com.strato.skylift.notice.service;

import com.strato.skylift.entity.Notice;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.repository.NoticePartRepository;
import com.strato.skylift.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoticePartService {

    private MyPageRepository myPageRepository;

    private NoticeRepository noticeRepository;

    private ModelMapper modelMapper;

    private NoticePartRepository noticePartRepository;

    public Optional<Notice> getNoticeByCode(Long noticeCode) {
        // 공지사항 코드에 해당하는 데이터 조회 로직을 구현합니다.
        Optional<Notice> noticeEntity = noticePartRepository.findByNoticeCode(noticeCode);
        return noticeEntity;
    }



}



    //멤버코드가 1에 해당하는 부서별 공지사항 글 가져오기

