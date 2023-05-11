package com.strato.skylift.equipment.service;

import com.strato.skylift.entity.Equipment;
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
    private final EQcategoryRepositroy equiCategoryRepositroy;
    private final EquipmentRepositroy equipmentRepositroy;
    private final ModelMapper modelMapper;

    public EquipmentService(EQcategoryRepositroy equiCategoryRepositroy ,
                            EquipmentRepositroy equipmentRepositroy, ModelMapper modelMapper)
    {
        this.equipmentRepositroy = equipmentRepositroy;
        this.equiCategoryRepositroy = equiCategoryRepositroy;
        this.modelMapper = modelMapper;
    }

    public Page<EquipmentDTO> selectEqiAll(int page)
    {
        Pageable pageable = PageRequest.of(page -1 , 8, Sort.by("EquipmentCode").descending());
        Page<Equipment> equipment = equipmentRepositroy.findAll(pageable);
        log.info("[EquipmentService] equipment : " + equipment.getContent());
        Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
        return equipmentDTO;
    }
}
