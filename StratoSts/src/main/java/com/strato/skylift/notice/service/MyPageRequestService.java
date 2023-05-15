package com.strato.skylift.notice.service;

import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import com.strato.skylift.member.repository.MyPageRepository;
import com.strato.skylift.notice.dto.RequestDto;
import com.strato.skylift.notice.repository.RequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class MyPageRequestService {

    private final RequestRepository requestRepository;

    private final ModelMapper modelMapper;

    private final MyPageRepository myPageRepository;

    //모든 리퀘스트 찾기
    public List<Request> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        log.info("findAll : {}", requests);
        return requests;
    }

    public List<Request> getMemberRequests(Long memberCode) {
        Member member = myPageRepository.findById(memberCode)
                .orElseThrow(() -> new IllegalArgumentException("No member found with id " + memberCode));
        log.info("Received requests : {}", member);
        return requestRepository.findRequestByMember(member);
    }



//수정 서비스
@Transactional
public void updateRequest(Member member, Long requestCode, RequestDto requestDto) {
    Request request = requestRepository.findById(requestCode)
            .orElseThrow(() -> new IllegalArgumentException("No request found with id " + requestCode));

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date requestStart = null;
    Date requestEnd = null;
    try {
        requestStart = formatter.parse(requestDto.getRequestStart());
        requestEnd = formatter.parse(requestDto.getRequestEnd());
    } catch (ParseException e) {
        e.printStackTrace();
    }

    request.setMember(member);
    request.setRequestReason(requestDto.getRequestReason());
    request.setRequsetType(requestDto.getRequsetType());
    request.setRequestStart(requestStart);
    request.setRequestEnd(requestEnd);

    requestRepository.save(request);
}



    //인서트 하기 근데 날짜를 ...ㅠㅠ
    @Transactional
    public void insertRequest(Member member, RequestDto requestDto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date requestStart = null;
        Date requestEnd = null;
        try {
            requestStart = formatter.parse(requestDto.getRequestStart());
            requestEnd = formatter.parse(requestDto.getRequestEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Request request = new Request();
        request.setMember(member);
        request.setRequestReason(requestDto.getRequestReason());
        request.setRequsetType(requestDto.getRequsetType());
        request.setRequestStart(requestStart);
        request.setRequestEnd(requestEnd);

        requestRepository.save(request);
    }

}
