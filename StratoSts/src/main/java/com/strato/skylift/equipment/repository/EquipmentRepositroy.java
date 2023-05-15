package com.strato.skylift.equipment.repository;

import com.strato.skylift.common.paging.Pagenation;
import com.strato.skylift.entity.Equipment;
import com.strato.skylift.equipment.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepositroy extends JpaRepository<Equipment , Long>
{
    @EntityGraph(attributePaths = {"equCategory.categoryCode"} ,type = EntityGraph.EntityGraphType.FETCH)
    Page<Equipment> findAll(Pageable pageable);

    @Query(value = "SELECT COUNT(e.equCategory.categoryCode) " +
            "FROM Equipment e " +
            "WHERE e.equipmentStatus <> '결제대기'" +
            "GROUP BY e.equCategory.categoryCode")
    List<Long> countByEquCategoryCategoryCode();

    @EntityGraph(attributePaths = {"equCategory.categoryCode"} ,type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT e FROM Equipment  e WHERE e.equCategory.categoryCode = :categoryCode AND e.equipmentStatus <> '결제대기' ORDER BY e.equipmentCode")
    Page<Equipment> findByCategoryCode(@Param("categoryCode")Long categoryCode, Pageable pageable);
}
