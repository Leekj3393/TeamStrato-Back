package com.strato.skylift.equipment.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.entity.EquCategory;
import com.strato.skylift.equipment.dto.*;
import com.strato.skylift.equipment.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/skylift/equipment")
public class EquipmentController
{
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService)
    {
        this.equipmentService = equipmentService;
    }


    @GetMapping("findCategory")
    public ResponseEntity<ResponseDto> find(@RequestParam(name = "page",defaultValue = "1")int page)
    {
        Page<CSequipmentCatgoryDTO> category = equipmentService.selectCategoryAll(page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(category);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(category.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

    @GetMapping("/detail/{categoryCode}")
    public ResponseEntity<ResponseDto> detail(@RequestParam(name = "page")int page
                                        , @PathVariable Long categoryCode)
    {
        log.info("equipmentCode : " + categoryCode);
        Page<EquipmentDTO> equipment = equipmentService.findByCategory(categoryCode,page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

    @GetMapping("/regist")
    public ResponseEntity<ResponseDto> registEquipment()
    {
        List<EquiCategoryDTO> equiCategoryDTO = equipmentService.findByCategoryAll();

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",equiCategoryDTO));
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> regist(@ModelAttribute EquipmentRegistDTO formData)
    {
        log.info("[regist] formData {} : " , formData);
        log.info("[regist] formData.files {} : " , formData.getImage());

        equipmentService.regist(formData);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"등록성공"));
    }

    @GetMapping("/modify")
    public ResponseEntity<ResponseDto> selectModify(@RequestParam(name = "page", defaultValue = "1") int page)
    {
        Page<EquipmentDTO> equipment = equipmentService.findEquipmentAll(page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

    @GetMapping("/modify/{categoryCode}")
    public ResponseEntity<ResponseDto> findCategoryList(@PathVariable Long categoryCode)
    {
        List<EquiCategoryDTO> categoryList = equipmentService.findCategoryList(categoryCode);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"ㅎㅇ",categoryList));
    }

    @GetMapping("/modify/serach")
    public ResponseEntity<ResponseDto> searchEquipment(@RequestParam(name = "page", defaultValue = "1") int page,
                                                       @RequestParam(name= "type") String searchName , @RequestParam(name = "value") String value)
    {
        log.info("type : {}" , searchName);
        log.info("value : {} " , value);

        Page<EquipmentDTO> equipment = equipmentService.selectCategorySerch(searchName, value , page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"test",responseDtoWithPaging));
    }

    @PostMapping("/statusUpdate")
    public ResponseEntity<ResponseDto> statusUpdate(@ModelAttribute ApprovalEquipmentDTO approvalEquipmentDTO)
    {
        equipmentService.statusUpdate(approvalEquipmentDTO);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"완료"));
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyEqu(@ModelAttribute EquipmentDTO equipmentDTO)
    {
        equipmentService.modifyEquipment(equipmentDTO);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"넹"));
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseDto> deleteUpdate(@RequestBody Long[] code)
    {
        equipmentService.deleteStatus(code);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"완료"));
    }



    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteEqu(@ModelAttribute EquipmentDTO equipmentDTO)
    {

        return null;
    }

    @GetMapping("/modifyList")
    public ResponseEntity<ResponseDto> modifyList(@RequestParam(name = "page" , defaultValue = "1")int page)
    {


        return null;
    }
}
