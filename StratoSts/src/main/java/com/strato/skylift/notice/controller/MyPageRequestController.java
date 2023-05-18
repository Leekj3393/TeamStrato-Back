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
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/skylift/myPage")
public class MyPageRequestController {

    private MyPageRequestService myPageRequestService;

//    @GetMapping("/request")
//    public ResponseEntity<List<Request>> getAllRequests(@AuthenticationPrincipal MbMemberDto memberDto) {
//        List<Request> requests = myPageRequestService.getAllRequests();
//
//        return ResponseEntity.ok(requests);
//    }

    @GetMapping("/request")
    public ResponseEntity<List<Request>> getAllRequests(@AuthenticationPrincipal MbMemberDto memberDto) {
        List<Request> requests = myPageRequestService.getAllRequests(memberDto.getMemberCode());

        return ResponseEntity.ok(requests);
    }



//    //로그인한 멤버의 코드로 리퀘스트 수정하기
//    @PatchMapping("/request/modify/{memberCode}/{requestCode}")
//    public ResponseEntity<ResponseDto> updateMember(@PathVariable Long memberCode, @PathVariable Long requestCode, @ModelAttribute RequestDto requestDto) {
//        Member member = myPageRepository.findById(memberCode)
//                .orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberCode));
//        myPageRequestService.updateRequest(member, requestCode, requestDto);
//
//        return ResponseEntity
//                .ok()
//                .body(new ResponseDto(HttpStatus.OK, "리퀘스트 수정 완료"));
//    }
//
//
    //리퀘스트요청 인서트
//@PostMapping("/request/insert")
//public ResponseEntity<ResponseDto> insertRequestGo(@AuthenticationPrincipal MbMemberDto memberDto, @RequestBody RequestDto requestDto) {
//    myPageRequestService.insertRequest(memberDto.getMemberCode(), requestDto);
//    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "리퀘스트 등록 완료"));
//}







}



