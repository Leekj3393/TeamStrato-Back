package com.strato.skylift.equipment.repository;

import com.strato.skylift.entity.Equipment;
import com.strato.skylift.equipment.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepositroy extends JpaRepository<Equipment , Long>
{
    @EntityGraph(attributePaths = {"equCategory.categoryCode"} ,type = EntityGraph.EntityGraphType.FETCH)
    Page<Equipment> findAll(Pageable pageable);

    Long countByEquCategoryCategoryCode(Long categoryCode);

}
