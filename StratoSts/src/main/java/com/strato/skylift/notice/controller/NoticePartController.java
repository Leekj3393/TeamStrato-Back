package com.strato.skylift.notice.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.Department;
import com.strato.skylift.notice.dto.NoticeDto;
import com.strato.skylift.notice.service.NoticePartService;
import com.strato.skylift.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/skylift/myPage")
public class NoticePartController {

    private final NoticeService noticeService;
    private final NoticePartService noticePartService;

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







}
