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

import java.util.List;


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
        List<Long> count = equipmentRepositroy.countByEquCategoryCategoryCode();
        for(int i = 0; i < cSequipmentCatgoryDTO.getContent().size(); i++)
            cSequipmentCatgoryDTO.getContent().get(i).setCategoryCount(count.get(i));
        return cSequipmentCatgoryDTO;
    }

    public Page<EquipmentDTO> selectCategorySerch(String name , String value , int page)
    {
        Pageable pageable = PageRequest.of(page - 1, 8 , Sort.by("equipmentCode").ascending());
        if(name.equals("EquipmentName"))
        {

            log.info("g2");
            Page<Equipment> equipment = equipmentRepositroy.findByCategoryCodeCategoryName(value,pageable);

            return equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
        }
        else if(name.equals("category"))
        {
            log.info("ㅎ2");
            Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(Long.parseLong(value),pageable);

            return equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
        }

        return null;
    };

    public Page<EquipmentDTO> findByCategory(Long category, int page)
    {
        Pageable pageable = PageRequest.of(page - 1, 8 , Sort.by("equipmentCode").ascending());

        Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(category,pageable);

        return equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
    }

    public void regist(EquipmentDTO equipmentDTO)
    {
        equipmentRepositroy.save(modelMapper.map(equipmentDTO , Equipment.class));
    }




}
