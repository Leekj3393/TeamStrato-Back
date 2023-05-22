package com.strato.skylift.education.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.education.dto.EducationDto;
import com.strato.skylift.education.service.EducationService;
import com.strato.skylift.member.dto.MbMemberDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/skylift/education")
public class EducationController {

	private final EducationService edService;
	
	public EducationController(EducationService edService) {
		this.edService = edService;
	}
	
	/* 교육 목록 전체 조회 */
	@GetMapping("/educations")
	public ResponseEntity<ResponseDto> selectProductList(@RequestParam(name="page", defaultValue="1") int page) {
		
		log.info("[ProductController] : selectProductList start =================================");
		log.info("[ProductController] : page : {}", page);
		
		Page<EducationDto> educationDtoList = edService.selectEducationList(page);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(educationDtoList);
		
//		log.info("[ProductController] : pageInfo : {}", pageInfo);
		
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(educationDtoList.getContent());
		
		
		
//		log.info("[ProductController] : selectProductList end =================================");
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", responseDtoWithPaging));
	}
	
	/* 교육 등록 */
	@PostMapping("/add")
	public ResponseEntity<ResponseDto> insertMember(@ModelAttribute EducationDto educationDto) {
		
		log.info("educationDto : {}", educationDto); 
		
		edService.insertMember(educationDto);
		
//		log.info("memberDto : {}", educationDto);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "교육 등록 성공"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
