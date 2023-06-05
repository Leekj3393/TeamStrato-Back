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
import org.springframework.cache.jcache.interceptor.JCacheOperationSource;
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
import java.util.Arrays;
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
        if(name.equals("equipmentName"))
        {
            log.info("selectCategorySerch g2");
            Page<Equipment> equipment = equipmentRepositroy.findByEquipmentNameLike(value,pageable);
            Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
            equipmentDTO.forEach(e -> log.info("e : {}", e));
            for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
            {
                equipmentDTO.getContent().get(i).getFile().setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFile().getFilePath());
            }
            return equipmentDTO;
        }
        else if(name.equals("equipmentCode"))
        {
            Page<Equipment> equipment = equipmentRepositroy.findById(Long.parseLong(value),pageable);
            Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
            for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
            {
                equipmentDTO.getContent().get(i).getFile().setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFile().getFilePath());
            }
            return equipmentDTO;
        }
        else if(name.equals("categoryName"))
        {
            log.info("ㅎ2");
            Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(Long.parseLong(value),pageable);
            Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
            for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
            {
                equipmentDTO.getContent().get(i).getFile().setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFile().getFilePath());
            }
            return equipmentDTO;
        }

        return null;
    };

    public Page<EquipmentDTO> findByCategory(Long categoryCode, int page)
    {
        log.info("image url" + IMAGE_URL  + "equipment/");
        Pageable pageable = PageRequest.of(page - 1, 4 , Sort.by("equipmentCode").ascending());
        Page<Equipment> equipment = equipmentRepositroy.findByCategoryCode(categoryCode,pageable);
        log.info("A1");
        Page<EquipmentDTO> equipmentDTO = equipment.map(equ -> modelMapper.map(equ , EquipmentDTO.class));
        log.info("A2");
        for(int i = 0 ; i < equipmentDTO.getContent().size(); i++)
        {
            equipmentDTO.getContent().get(i).getFile().setFilePath(IMAGE_URL + "equipment/" + equipmentDTO.getContent().get(i).getFile().getFilePath());
        }
        log.info("A4");
        return equipmentDTO;
    }

    public Page<EquipmentDTO> findEquipmentAll(int page)
    {
        log.info("image url" + IMAGE_URL  + "equipment/");
        Pageable pageable = PageRequest.of(page - 1, 4 , Sort.by("equipmentCode").ascending());

        Page<Equipment> equipment = equipmentRepositroy.findEquipmentAll(pageable);

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
                String reFileName = FileUploadUtils.saveFile(IMAGE_DIR + "equipment/", imageName, equipmentRegistDTO.getImage());
                EQFileDTO file = new EQFileDTO(imageName , reFileName , "장비");
                log.info("regist : file : {} " , file);
                equipmentRegistDTO.setFile(file);

                Equipment equipment = modelMapper.map(equipmentRegistDTO , Equipment.class);
                equipment.setEquipmentCreateDate(date);
                equipment.setEquipmentStatus("wait");

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
    public void statusUpdate(ApprovalEquipmentDTO approvalEquipmentDTO)
    {
        EquipmentApproval approval = modelMapper.map(approvalEquipmentDTO, EquipmentApproval.class);
        approval.setAppRegistDate(new Date());
        eqApprovalRepositroy.save(approval);
    }

    @Transactional
    public void modifyEquipment(EquipmentDTO equipmentDTO)
    {
        log.info("[modifyEquipment]equipmentDTO : {}",equipmentDTO);
        Equipment equipment = equipmentRepositroy.findById(equipmentDTO.getEquipmentCode()).orElseThrow(() ->
                new IllegalArgumentException("코드의 해당 상품 없다. : " + equipmentDTO.getEquipmentCode()));
        EquCategory category = eqCategoryRepositroy.findById(equipmentDTO.getEquCategory().getCategoryCode())
                .orElseThrow(() -> new IllegalArgumentException("코드의 해당 상품 없다. : " + equipmentDTO.getEquipmentCode()));
        try
        {
            if(equipmentDTO.getEquipmentImage() != null)
            {
                log.info("[modifyEquipment] A1-1");
                String imageName = UUID.randomUUID().toString().replace("-","");
                String reFileName = FileUploadUtils.saveFile(IMAGE_DIR + "equipment/", imageName, equipmentDTO.getEquipmentImage());
                log.info("[modifyEquipment] A1-2");
                log.info("[modifyEquipment]equipment.getFile().getFilePath() : {} " , equipment.getFile().getFilePath());
                FileUploadUtils.deleteFile(IMAGE_DIR + "equipment/" , equipment.getFile().getFilePath());
                log.info("[modifyEquipment] A1-3");
                equipment.getFile().setFileName(imageName);
                equipment.getFile().setFilePath(reFileName);
            }
            log.info("[modifyEquipment] A2");

            equipment.update(category
                            ,equipmentDTO.getEquipmentName()
                            ,new Date()
                            ,equipmentDTO.getEquipmentStatus());
            log.info("[modifyEquipment] A3");
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public List<EquiCategoryDTO> findByCategoryAll()
    {
        List<EquCategory> equCategoryList = eqCategoryRepositroy.findByEquCategoryCategoryCodeIsNotNull();

        List<EquiCategoryDTO> equiCategoryDTO = equCategoryList.stream()
                .map(category -> modelMapper.map(category , EquiCategoryDTO.class)).collect(Collectors.toList());
        return equiCategoryDTO;
    }

    public List<EquiCategoryDTO> findCategoryList(Long categoryCode)
    {
        List<EquCategory> categoryList = eqCategoryRepositroy.findByCategoryName(categoryCode);

        return categoryList.stream().map(c -> modelMapper.map(c, EquiCategoryDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public void deleteStatus(Long[] code)
    {
        List<Long> c = Arrays.asList(code);

        List<Equipment> equipment = equipmentRepositroy.findByEquipmentCodeIn(c);

        equipment.forEach(e -> { e.setEquipmentStatus("폐기"); e.setEquipmentModifyDate(new Date()); });
    }
}
