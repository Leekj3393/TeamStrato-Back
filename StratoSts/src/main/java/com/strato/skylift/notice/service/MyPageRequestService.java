package com.strato.skylift.notice.service;

import com.strato.skylift.entity.Request;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.repository.ApprovalRepositoryMyPage;
import com.strato.skylift.notice.repository.RequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class MyPageRequestService {

    private final RequestRepository requestRepository;

    private final ModelMapper modelMapper;

    private final ApprovalRepositoryMyPage approvalRepository;

    private final MyPageRepository myPageRepository;

    //모든 리퀘스트 찾기
    //모든 리퀘스트 찾기
//    public List<Request> getAllRequests() {
//        return requestRepository.findAllWithApprovalsAndMembers();
//    }
    public List<Request> getAllRequests(Long memberCode) {
        return approvalRepository.findAllRequestsByMemberCode(memberCode);
    }


//    public List<Request> getMemberRequests(Long memberCode) {
//        Member member = myPageRepository.findById(memberCode)
//                .orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberCode));
//        log.info("Received requests : {}", member);
//        return requestRepository.findRequestByMember(member);
//    }


//
////수정 서비스
//@Transactional
//public void updateRequest(Member member, Long requestCode, RequestDto requestDto) {
//    Request request = requestRepository.findById(requestCode)
//            .orElseThrow(() -> new IllegalArgumentException("No request found with id " + requestCode));
//
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    Date requestStart = null;
//    Date requestEnd = null;
//    try {
//        requestStart = formatter.parse(requestDto.getRequestStart());
//        requestEnd = formatter.parse(requestDto.getRequestEnd());
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }
//
//    request.getApprovals(member);
//    request.setRequestReason(requestDto.getRequestReason());
//    request.setRequsetType(requestDto.getRequsetType());
//    request.setRequestStart(requestStart);
//    request.setRequestEnd(requestEnd);
//
//    requestRepository.save(request);
//}
//
//
//
//    @Transactional
//    public void insertRequest(Member member, RequestDto requestDto) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date requestStart = null;
//        Date requestEnd = null;
//        try {
//            requestStart = formatter.parse(requestDto.getRequestStart());
//            requestEnd = formatter.parse(requestDto.getRequestEnd());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // Check if the member has already made a request of the same type
//        List<Request> existingRequests = requestRepository.findRequestByMemberAndRequsetType(member, requestDto.getRequsetType());
//        if (!existingRequests.isEmpty()) {
//            throw new RuntimeException(requestDto.getRequsetType() + " 신청은 1번 이상 신청할 수 없습니다.");
//        }
//
//
//        Request request = new Request();
//        request.setMember(member);
//        request.setRequestReason(requestDto.getRequestReason());
//        request.setRequsetType(requestDto.getRequsetType());
//        request.setRequestStart(requestStart);
//        request.setRequestEnd(requestEnd);
//
//        requestRepository.save(request);
//    }


}
