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
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("아이디를 다시 확인해주세요: " + memberCode));

        if ("퇴직 신청".equals(requestDto.getRequsetType())) {
            List<Request> requests = requestRepository.findAllByApprovals_Member(member);
            boolean hasResignationRequest = requests.stream()
                    .anyMatch(request -> "퇴직 신청".equals(request.getRequsetType()));
            if (hasResignationRequest) {
                throw new IllegalStateException("이미 퇴직 요청이 있습니다. 더 이상 퇴직 신청을 할 수 없습니다.");
            }
        }

        Request newRequest = new Request();
        newRequest.setRequestReason(requestDto.getRequestReason());
        newRequest.setRequsetType(requestDto.getRequsetType());
        newRequest.setRequestStart(requestDto.getRequestStart());
        newRequest.setRequestEnd(requestDto.getRequestEnd());
        Request savedRequest = requestRepository.save(newRequest);

        Approval newApproval = new Approval();
        newApproval.setMember(member);
        newApproval.setRequest(savedRequest);
        newApproval.setAppTitle(requestDto.getRequsetType());
        newApproval.setAppContent(requestDto.getRequestReason());
        newApproval.setAppType(requestDto.getRequsetType());
        newApproval.setAppStatus("wait");
        newApproval.setAppRegistDate(new Date());

        approvalRepository.save(newApproval);
    }

    public void deleteRequest(Long requestCode) {
        Request request = requestRepository.findById(requestCode).orElseThrow(() ->
                new IllegalArgumentException("아이디를 다시 확인해주세요: " + requestCode));

        Approval approval = (Approval) approvalRepository.findByRequest(request)
                .orElseThrow(() -> new IllegalArgumentException("승인 요청을 찾을 수 없습니다: " + requestCode));

        if (!"wait".equals(approval.getAppStatus())) {
            throw new IllegalStateException("이 요청은 'wait' 상태가 아니므로 삭제할 수 없습니다.");
        }

        requestRepository.delete(request);
    }




}
