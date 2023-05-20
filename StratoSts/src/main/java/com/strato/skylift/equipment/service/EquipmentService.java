package com.strato.skylift.equipment.service;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.EquCategory;
import com.strato.skylift.entity.Equipment;
import com.strato.skylift.entity.File;
import com.strato.skylift.equipment.dto.*;
import com.strato.skylift.equipment.entity.EquipmentFile;
import com.strato.skylift.equipment.entity.EquipmentRegist;
import com.strato.skylift.equipment.repository.EQcategoryRepositroy;
import com.strato.skylift.equipment.repository.EquipmentRepositroy;
import com.strato.skylift.equipment.repository.FileRepositrory;
import com.strato.skylift.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.Caching;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EquipmentService
{
    private final EQcategoryRepositroy eqCategoryRepositroy;
    private final EquipmentRepositroy equipmentRepositroy;
    private final FileRepositrory fileRepositrory;
    private final ModelMapper modelMapper;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public EquipmentService(EQcategoryRepositroy eqCategoryRepositroy ,
                            EquipmentRepositroy equipmentRepositroy,
                            FileRepositrory fileRepositrory ,ModelMapper modelMapper)
    {
        this.equipmentRepositroy = equipmentRepositroy;
        this.eqCategoryRepositroy = eqCategoryRepositroy;
        this.fileRepositrory = fileRepositrory;
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
        log.info("image url" + IMAGE_URL );
        Pageable pageable = PageRequest.of(page - 1, 4 , Sort.by("equipmentCode").ascending());

        Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(category,pageable);

        Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));

        for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
        {
            equipmentDTO.getContent().get(i).getFiles().get(i).setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFiles().get(i).getFilePath());
        }
        return equipmentDTO;
    }

    public void regist(EquipmentRegistDTO equipmentRegistDTO)
    {
        Date date = new Date();
        log.info("data : " + date);

        if(equipmentRegistDTO.getImage() != null)
        {
            try
            {
                String imageName = UUID.randomUUID().toString().replace("-", "");
                String reFileName = FileUploadUtils.saveFile(IMAGE_DIR + "equipment", imageName, equipmentRegistDTO.getImage());
                equipmentRegistDTO.getFiles().get(0).setFilePath(reFileName);
                equipmentRegistDTO.getFiles().get(0).setFileName(imageName);
                equipmentRegistDTO.getFiles().get(0).setFileType("장비");
                EquipmentRegist equipment = modelMapper.map(equipmentRegistDTO , EquipmentRegist.class);
                log.info("equipment : " + equipment);

            }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    @Transactional
    public void modifyEquipment(EquipmentDTO equipmentDTO)
    {
        Equipment equipment = equipmentRepositroy.findById(equipmentDTO.getEquipmentCode()).orElseThrow(() ->
                new IllegalArgumentException("코드의 해당 상품 없다. : " + equipmentDTO.getEquipmentCode()));
        EquipmentFile file = fileRepositrory.findByEquipmentCode(equipmentDTO.getEquipmentCode());
        try
        {
            if(equipmentDTO.getEquipmentImage() != null)
            {
                String imageName = UUID.randomUUID().toString().replace("-","");
                String reFileName = FileUploadUtils.saveFile(IMAGE_DIR + "equipment", imageName, equipmentDTO.getEquipmentImage());

                FileUploadUtils.deleteFile(IMAGE_DIR , file.getFilePath());

                file.setFilePath(reFileName);
            }
            equipment.update(equipmentDTO.getEquCategory().getCategoryCode()
                            ,equipmentDTO.getEquipmentName()
                            ,new Date()
                            ,equipmentDTO.getEquipmentStatus());
        }
        catch (IOException e) { throw new RuntimeException(e); }



    }

    @Transactional
    public void saveFile(EQFileDTO eqFileDTO)
    {
        String imageName = UUID.randomUUID().toString().replace("-","");
        try {
            String reFileName = FileUploadUtils.saveFile(IMAGE_DIR + "equipment", imageName, eqFileDTO.getEquipmentImage());
            eqFileDTO.setFilePath(reFileName);
            EquipmentFile file = modelMapper.map(eqFileDTO , EquipmentFile.class);
            fileRepositrory.save(file);
        }
        catch (IOException e) { throw new RuntimeException(e);}

    }

    public List<EquiCategoryDTO> findByCategoryAll()
    {
        List<EquCategory> equCategoryList = eqCategoryRepositroy.findByEquCategoryCategoryCodeIsNotNull();

        List<EquiCategoryDTO> equiCategoryDTO = equCategoryList.stream()
                .map(category -> modelMapper.map(category , EquiCategoryDTO.class)).collect(Collectors.toList());
        return equiCategoryDTO;
    }
}
