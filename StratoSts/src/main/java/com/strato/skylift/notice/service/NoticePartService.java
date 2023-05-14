package com.strato.skylift.notice.service;

import com.strato.skylift.entity.Department;
import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Notice;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.repository.NoticePartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoticePartService {

    private MyPageRepository myPageRepository;

    private NoticePartRepository noticePartRepository;

    private ModelMapper modelMapper;
}



    //멤버코드가 1에 해당하는 부서별 공지사항 글 가져오기

