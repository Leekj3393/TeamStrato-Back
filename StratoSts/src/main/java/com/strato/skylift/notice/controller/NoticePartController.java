package com.strato.skylift.notice.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.Notice;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.repository.NoticePartRepository;
import com.strato.skylift.notice.service.NoticePartService;
import com.strato.skylift.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/skylift/myPage")
public class NoticePartController {

    private final NoticeService noticeService;
    private final NoticePartService noticePartService;
    private final ModelMapper modelMapper;
    private final NoticePartRepository noticePartRepository;

    //전체 공지사항 조회
    @GetMapping("/notice")
    public ResponseEntity<ResponseDto> selectNoticeList(@RequestParam(name="page", defaultValue="1") int page){

        log.info("[NoticePartController] : page : {}", page);

        Page<NoticeDto> noticeDtoList = noticeService.selectNoticeList(page);
        PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);

        log.info("[ProductController] : pageInfo : {}", pageInfo);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(pageInfo);
        responseDtoWithPaging.setData(noticeDtoList.getContent());


        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }

    //해당하는 공지사항 코드 조회
    @GetMapping("/notice/part/{noticeCode}")
    public ResponseEntity<ResponseDto> selectNoticeCode(@PathVariable("noticeCode") Long noticeCode) {
        Optional<Notice> noticeOptional = noticePartRepository.findByNoticeCode(noticeCode);

        if (noticeOptional.isPresent()) {
            Notice notice = noticeOptional.get();
            ResponseDto responseDto = new ResponseDto(HttpStatus.OK, "조회 성공", notice);
            return ResponseEntity.ok().body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }




    //로그인된 회원의 부서에 맞는 공지사항 불러오기
    @GetMapping("/notice/part")
    public ResponseEntity<ResponseDto> getNoticesForAuthenticatedUser(
            @AuthenticationPrincipal MbMemberDto member,
            @RequestParam(name="page", defaultValue="1") int page) {

        //멤버의 부서 코드 가져오기
        String deptCode = member.getDepartment().getDeptCode();
        Page<Notice> noticeList = noticeService.getNoticesByDeptCode(deptCode, page);
        Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
        PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(pageInfo);
        responseDtoWithPaging.setData(noticeDtoList.getContent());

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }


    //공지사항 서치하기
    @GetMapping("/notice/part/search")
    public ResponseEntity<ResponseDto> searchNoticesForAuthenticatedUser(
            @AuthenticationPrincipal MbMemberDto member,
            @RequestParam(name="page", defaultValue="1") int page,
            @RequestParam(name="title") String title) {

        String deptCode = member.getDepartment().getDeptCode();
        Page<Notice> noticeList = noticePartService.findNoticeByTitleAndDeptCode(title, deptCode, page);
        Page<NoticeDto> noticeDtoList = noticeList.map(notice -> modelMapper.map(notice, NoticeDto.class));
        PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(noticeDtoList);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(pageInfo);
        responseDtoWithPaging.setData(noticeDtoList.getContent());

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }



    //공지사항 인서트하기
    @PostMapping("/notice/insert")
    public ResponseEntity<?> createNotice(@AuthenticationPrincipal MbMemberDto member,
                                          @RequestBody NoticeDto noticeDto) {
        if (!"J2".equals(member.getJob().getJobCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("글을 쓸 수 있는 권한이 없습니다.");
        }

        Notice notice = noticeService.createNotice(noticeDto);
        return ResponseEntity.ok(notice);
    }


    //삭제

    //메신저







}
