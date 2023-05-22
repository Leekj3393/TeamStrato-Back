package com.strato.skylift.equipment.service;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.EquCategory;
import com.strato.skylift.entity.Equipment;
import com.strato.skylift.entity.File;
import com.strato.skylift.equipment.dto.*;
import com.strato.skylift.equipment.entity.EquipmentApproval;
import com.strato.skylift.equipment.entity.EquipmentFile;
import com.strato.skylift.equipment.entity.EquipmentRegist;
import com.strato.skylift.equipment.repository.EQcategoryRepositroy;
import com.strato.skylift.equipment.repository.EqApprovalRepositroy;
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
    private final EqApprovalRepositroy eqApprovalRepositroy;
    private final ModelMapper modelMapper;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public EquipmentService(EQcategoryRepositroy eqCategoryRepositroy ,
                            EquipmentRepositroy equipmentRepositroy,
                            FileRepositrory fileRepositrory ,
                            EqApprovalRepositroy eqApprovalRepositroy , ModelMapper modelMapper)
    {
        this.equipmentRepositroy = equipmentRepositroy;
        this.eqCategoryRepositroy = eqCategoryRepositroy;
        this.fileRepositrory = fileRepositrory;
        this.eqApprovalRepositroy = eqApprovalRepositroy;
        this.modelMapper = modelMapper;
    }


    public Page<CSequipmentCatgoryDTO> selectCategoryAll(int page)
    {
        Pageable pageable = PageRequest.of(page - 1, 8 , Sort.by("categoryCode").ascending());
        Page<EquCategory> category = eqCategoryRepositroy.findAll(pageable);

        List<Object> maxCreateDate = equipmentRepositroy.findMaxCreateDate();
        List<Object> maxModifyDate = equipmentRepositroy.findMaxModifyDate();
        Page<CSequipmentCatgoryDTO> cSequipmentCatgoryDTO = category.map(equ -> modelMapper.map(equ , CSequipmentCatgoryDTO.class));

        List<Long> count = equipmentRepositroy.countByEquCategoryCategoryCode();
        log.info("count : " + count );
        maxModifyDate.forEach(m -> log.info("m : " + m));
        for(int i = 0; i < count.size(); i++)
        {
            cSequipmentCatgoryDTO.getContent().get(i).setCategoryCount(count.get(i));
            cSequipmentCatgoryDTO.getContent().get(i).setEquipmentCreateDate((Date) maxCreateDate.get(i));
            cSequipmentCatgoryDTO.getContent().get(i).setEquipmentModifyDate((Date) maxModifyDate.get(i));
        }

        cSequipmentCatgoryDTO.forEach(cs ->log.info("cs : {}" , cs));
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
        log.info("image url" + IMAGE_URL  + "equipment/");
        Pageable pageable = PageRequest.of(page - 1, 4 , Sort.by("equipmentCode").ascending());

        Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(category,pageable);

        Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));

        for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
        {
            equipmentDTO.getContent().get(i).getFile().setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFile().getFilePath());
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
                EQFileDTO file = new EQFileDTO(imageName , reFileName , "장비");
                log.info("file : {} " , file);
                equipmentRegistDTO.setFile(file);

                Equipment equipment = modelMapper.map(equipmentRegistDTO , Equipment.class);
                equipment.setEquipmentCreateDate(date);
                equipment.setEquipmentStatus("결제 대기중");

                equipmentRepositroy.save(equipment);

                Long equipmentCode = equipmentRepositroy.findByMaxCode();

                EquipmentApproval approval = modelMapper.map(equipmentRegistDTO, EquipmentApproval.class);
                approval.getEquipmentCode().setEquipmentCode(equipmentCode);
                approval.setAppRegistDate(date);

                eqApprovalRepositroy.save(approval);

                log.info("equipment : " + equipment);
                log.info("approval : " + approval);

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
