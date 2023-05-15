package com.strato.skylift.equipment.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.equipment.dto.CSequipmentCatgoryDTO;
import com.strato.skylift.equipment.dto.EquipmentDTO;
import com.strato.skylift.equipment.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("searchEquipment/{searchName}")
    public ResponseEntity<ResponseDto> searchEquipment(@RequestParam(name = "page", defaultValue = "1") int page,
                                                       @PathVariable String searchName , @RequestParam(name = "value") String value)
    {
        Page<EquipmentDTO> equipment = equipmentService.selectCategorySerch(searchName, value , page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"test",responseDtoWithPaging));
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseDto> detail(@RequestParam(name = "page")int page
                                        , @RequestParam(name = "category")Long category)
    {
        Page<EquipmentDTO> equipment = equipmentService.findByCategory(category,page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> regist(@ModelAttribute EquipmentDTO equipmentDTO)
    {
        equipmentService.regist(equipmentDTO);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"등록성공"));
    }

    @GetMapping("/modify")
    public ResponseEntity<ResponseDto> selectModify(@RequestParam(name = "page", defaultValue = "1") int page ,
                                                    @RequestParam(name = "category")Long category)
    {
        Page<EquipmentDTO> equipment = equipmentService.findByCategory(category,page);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(equipment);
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(equipment.getContent(),pagingButtonInfo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

    @PostMapping("/modify")
    public ResponseEntity<ResponseDto> modifyReqs(@ModelAttribute EquipmentDTO equipmentDTO)
    {

        return null;
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyEqu(@ModelAttribute EquipmentDTO equipmentDTO)
    {


        return null;
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
