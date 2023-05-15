package com.strato.skylift.equipment.service;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.Equipment;
import com.strato.skylift.equipment.dto.CSequipmentCatgoryDTO;
import com.strato.skylift.equipment.dto.EquipmentDTO;
import com.strato.skylift.equipment.repository.EQcategoryRepositroy;
import com.strato.skylift.equipment.repository.EquipmentRepositroy;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EquipmentService
{
    private final EQcategoryRepositroy eqCategoryRepositroy;
    private final EquipmentRepositroy equipmentRepositroy;
    private final ModelMapper modelMapper;

    public EquipmentService(EQcategoryRepositroy eqCategoryRepositroy ,
                            EquipmentRepositroy equipmentRepositroy,ModelMapper modelMapper)
    {
        this.equipmentRepositroy = equipmentRepositroy;
        this.eqCategoryRepositroy = eqCategoryRepositroy;
        this.modelMapper = modelMapper;
    }


    public Page<CSequipmentCatgoryDTO> selectCategoryAll(int page)
    {
        Pageable pageable = PageRequest.of(page - 1, 8 , Sort.by("equipmentCode").ascending());
        Page<Equipment> equipment = equipmentRepositroy.findAll(pageable);

        Page<CSequipmentCatgoryDTO> cSequipmentCatgoryDTO = equipment.map(equ -> modelMapper.map(equ , CSequipmentCatgoryDTO.class));
        cSequipmentCatgoryDTO.forEach(cs -> cs.setCategoryCount(equipmentRepositroy.countByEquCategoryCategoryCode(cs.getCategoryCode())));
        return cSequipmentCatgoryDTO;
    }
}
