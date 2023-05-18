package com.strato.skylift.equipment.repository;

import com.strato.skylift.entity.File;
import com.strato.skylift.equipment.entity.EquipmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepositrory extends JpaRepository<EquipmentFile , Long>
{
    EquipmentFile findByEquipmentCode(Long equipmentCode);
}
