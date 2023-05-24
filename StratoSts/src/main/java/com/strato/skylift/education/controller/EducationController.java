package com.strato.skylift.education.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.education.dto.ClassDto;
import com.strato.skylift.education.dto.EdFileDto;
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
	
	/* 교육 안전카테고리 조회 */
	@GetMapping("/safety")  
	public ResponseEntity<ResponseDto> selectEduSafetyList(@RequestParam(name="page", defaultValue="1") int page) {
			
		Page<EducationDto> educationDtoList = edService.selectEducationSafetyList(page);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(educationDtoList);
			
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(educationDtoList.getContent());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", responseDtoWithPaging));
	}
	
	/* 교육 직무카테고리 조회 */
	@GetMapping("/duty")
	public ResponseEntity<ResponseDto> selectEduDutyList(@RequestParam(name="page", defaultValue="1") int page) {
			
		Page<EducationDto> educationDtoList = edService.selectEducationDutyList(page);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(educationDtoList);
			
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(educationDtoList.getContent());
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", responseDtoWithPaging));
	}
	
	/* 교육 기타카테고리 조회 */
	@GetMapping("/other")
	public ResponseEntity<ResponseDto> selectEduOtherList(@RequestParam(name="page", defaultValue="1") int page) {
			
		Page<EducationDto> educationDtoList = edService.selectEducationOtherList(page);
		
		PagingButtonInfo pageInfo = Pagenation.getPagingButtonInfo(educationDtoList);
			
		ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
		responseDtoWithPaging.setPageInfo(pageInfo);
		responseDtoWithPaging.setData(educationDtoList.getContent());
		
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
	
	/* 교육 상세조회 */
	@GetMapping("/educations/{edCode}")
	public ResponseEntity<ResponseDto> selectEducationDetail(@PathVariable Long edCode) {
		
		EducationDto eduDto = edService.selectEducationDetail(edCode);
		EdFileDto fileDto = edService.selectEducationVideo(edCode);
		
		Map<String, Object> data = new HashMap<>();
		data.put("education", eduDto);
		data.put("video", fileDto);
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "직원 상세조회에 성공했습니다.", data));
	}
	
	/* 수강 등록 */
	@PostMapping("/classRegist")
	public ResponseEntity<ResponseDto> insertClass(@AuthenticationPrincipal MbMemberDto memberDto, @RequestParam(name="edCode") Long edCode) {
		
		System.out.println("동작");
		
		edService.insertClass(memberDto, edCode);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK, "수강 등록 성공"));
	}
	
	/* 수강 정보 업데이트 */
	@PutMapping("/classUpdate")
	public ResponseEntity<ResponseDto> updateClass(@AuthenticationPrincipal MbMemberDto memberDto, @RequestParam(name="edCode") Long edCode,
			@RequestParam(name="playTime") long classTime
			) {
		
		edService.updateClass(memberDto, edCode, classTime);
		
		return ResponseEntity
				.ok()
				.body(new ResponseDto(HttpStatus.OK,"수강 수정 성공"));
	}
	
	/* 수강한 수강정보 조회 */
	@GetMapping("/classView")
	public ResponseEntity<ResponseDto> selectClassView(@AuthenticationPrincipal MbMemberDto memberDto) {
		
		ClassDto classDto = edService.selectClassView(memberDto);
				
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회성공", classDto));
	}
}