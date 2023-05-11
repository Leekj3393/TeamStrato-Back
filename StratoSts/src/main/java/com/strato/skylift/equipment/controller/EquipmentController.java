package com.strato.skylift.equipment.controller;

import com.strato.skylift.common.ResponseDto;
import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.common.paging.PagingButtonInfo;
import com.strato.skylift.common.paging.ResponseDtoWithPaging;
import com.strato.skylift.equipment.dto.EquipmentDTO;
import com.strato.skylift.equipment.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/equipmentList")
    public ResponseEntity<ResponseDto> findAllEquipment(@RequestParam(name = "page",defaultValue = "1") int page)
    {

        Page<EquipmentDTO> equipment = equipmentService.selectEqiAll(page);
        log.info("[Equipment Controller] equiment : " + equipment.getContent());
        PagingButtonInfo pagingInfo = Pagenation.getPagingButtonInfo(equipment);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(pagingInfo);
        responseDtoWithPaging.setData(equipment.getContent());

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK,"성공",responseDtoWithPaging));
    }

}
