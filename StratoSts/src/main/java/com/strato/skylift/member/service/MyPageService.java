package com.strato.skylift.member.service;

import com.strato.skylift.member.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageRepository myPageRepository;
}
