package com.strato.skylift.equipment.repository;

import com.strato.skylift.equipment.entity.EquipmentApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EqApprovalRepositroy extends JpaRepository<EquipmentApproval ,Long >
{
}
