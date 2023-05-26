package com.strato.skylift.salary.service;

import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.SalaryStatement;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.salary.dto.SalaryDTO;
import com.strato.skylift.salary.repository.SLMemberRepository;
import com.strato.skylift.salary.repository.SalaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SalaryService
{
    private SalaryRepository salaryRepository;
    private SLMemberRepository slMemberRepository;
    private ModelMapper modelMapper;

    public SalaryService(SalaryRepository salaryRepository,
                         SLMemberRepository slMemberRepository,
                         ModelMapper modelMapper)
    {
        this.salaryRepository = salaryRepository;
        this.slMemberRepository = slMemberRepository;
        this.modelMapper = modelMapper;
    }

    public Page<SalaryDTO> findByMemberCode(MbMemberDto member , int page)
    {
        Pageable pageable = PageRequest.of(page -1 , 12 , Sort.by("salaryCode").descending());
        log.info("member : {}" , member);
        Page<SalaryStatement> salaryStatements = salaryRepository.findByMemberMemberId(member.getMemberCode(),pageable);

        Page<SalaryDTO> salaryDTO = salaryStatements.map(sal -> modelMapper.map(sal,SalaryDTO.class));

        salaryDTO.getContent().forEach(s -> s.setSalaryDay(s.getSalaleDate()));

        return salaryDTO;
    }

    public List<MbMemberDto> findByMemberNameLike(String value)
    {
        List<Member> memberList = slMemberRepository.findByMemberNameLike(value);

       memberList.forEach(m -> log.info("member : {}", m));

        return memberList.stream().map(m -> modelMapper.map(m, MbMemberDto.class)).collect(Collectors.toList());
    }
}
