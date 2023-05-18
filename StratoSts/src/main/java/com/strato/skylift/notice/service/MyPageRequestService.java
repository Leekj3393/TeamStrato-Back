package com.strato.skylift.notice.service;

import com.strato.skylift.approval.dto.ApprovalDto;
import com.strato.skylift.entity.Approval;
import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.dto.RequestDto;
import com.strato.skylift.notice.repository.ApprovalRepositoryMyPage;
import com.strato.skylift.notice.repository.RequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MyPageRequestService {

    private final RequestRepository requestRepository;

    private final ModelMapper modelMapper;

    private final ApprovalRepositoryMyPage approvalRepository;

    private final MyPageRepository myPageRepository;

    //모든 리퀘스트 찾기
    public List<Request> getAllRequests(Long memberCode) {
        return approvalRepository.findAllRequestsByMemberCode(memberCode);
    }


    //리 퀘스트 등록하기
    @Transactional
    public void insertRequest(Long memberCode, RequestDto requestDto) {
        // Find Member by memberCode
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("아이디를 다시 확인해주세요 :  " + memberCode));

        // Create new Request
        Request newRequest = new Request();
        newRequest.setRequestReason(requestDto.getRequestReason());
        newRequest.setRequsetType(requestDto.getRequsetType());
        newRequest.setRequestStart(requestDto.getRequestStart());
        newRequest.setRequestEnd(requestDto.getRequestEnd());
        Request savedRequest = requestRepository.save(newRequest);

        // Create new Approval
        Approval newApproval = new Approval();
        newApproval.setMember(member);
        newApproval.setRequest(savedRequest);
        newApproval.setAppTitle(requestDto.getRequsetType());
        newApproval.setAppContent(requestDto.getRequestReason());
        newApproval.setAppType(requestDto.getRequsetType());
        newApproval.setAppStatus("대기");
        newApproval.setAppRegistDate(new Date());
        newApproval.setApprovedDate(new Date());
        newApproval.setAppWdlDate(new Date());

        // Save new Approval
        approvalRepository.save(newApproval);
    }
    //리퀘스트 삭제하기
    public void deleteRequest(Long memberCode, RequestDto requestDto) {
        Request request = requestRepository.findById(memberCode).orElseThrow(() ->
                new IllegalArgumentException("아이디를 다시 확인해주세요 :  " + memberCode));

        requestRepository.deleteById(memberCode);
    }


}
