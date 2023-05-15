package com.strato.skylift.notice.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.dto.RequestDto;
import com.strato.skylift.notice.repository.RequestRepository;
import com.strato.skylift.notice.service.MyPageRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/skylift/myPage")
public class MyPageRequestController {

    private final RequestRepository requestRepository;
    private MyPageRequestService myPageRequestService;
    private MyPageRepository myPageRepository;

    @GetMapping("/request")
    public List<Request> getAllRequests() {
        List<Request> requests = myPageRequestService.getAllRequests();
        log.info("Received requests : {}", requests);
        return requests;
    }


    //로그인한 멤버의 코드 조회
    @GetMapping("/request/{memberCode}")
    public List<Request> getMemberRequests(@PathVariable Long memberCode) {
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("멤버 아이디 다시 확인하십 " + memberCode));
        log.info("Received requests: {}", member);
        return requestRepository.findRequestByMember(member);
    }
    //로그인한 멤버의 코드로 리퀘스트 수정하기
    @PatchMapping("/request/modify/{memberCode}/{requestCode}")
    public ResponseEntity<ResponseDto> updateMember(@PathVariable Long memberCode, @PathVariable Long requestCode, @ModelAttribute RequestDto requestDto) {
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberCode));
        myPageRequestService.updateRequest(member, requestCode, requestDto);

        return ResponseEntity
                .ok()
                .body(new ResponseDto(HttpStatus.OK, "리퀘스트 수정 완료"));
    }


    //리퀘스트요청 인서트
    @PostMapping("/request/insert/{memberCode}")
    public ResponseEntity<ResponseDto> insertRequestGo(@PathVariable Long memberCode, @RequestBody RequestDto requestDto) {
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberCode));
        myPageRequestService.insertRequest(member, requestDto);

        log.info("member: {}", member);

        return ResponseEntity
                .ok()
                .body(new ResponseDto(HttpStatus.OK, "리퀘스트 등록 완료"));
    }




}



