package com.strato.skylift.salary.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.salary.dto.SalaryDTO;
import com.strato.skylift.salary.service.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/salary")
public class SalaryController
{
    private SalaryService salaryService;

    public SalaryController(SalaryService salaryService)
    {
        this.salaryService = salaryService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> findByAllSalary(@AuthenticationPrincipal MbMemberDto member ,
                                                       @RequestParam(name = "page", defaultValue = "1") int page)
    {
        log.info("member : {} " , member);
        Page<SalaryDTO> salary = salaryService.findByMemberCode(member ,page);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"완료",salary));
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<ResponseDto> findBySearchEmp(@PathVariable String search)
    {
        log.info("search : {}" , search);
        List<MbMemberDto> member = salaryService.findByMemberNameLike(search);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"완료",member));
    }





}
