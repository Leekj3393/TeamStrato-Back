package com.strato.skylift.equipment.repository;

import com.strato.skylift.entity.EquCategory;
import com.strato.skylift.entity.Equipment;
import com.strato.skylift.equipment.dto.CSequipmentCatgoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EQcategoryRepositroy extends JpaRepository<EquCategory , Long>
{
    @EntityGraph(attributePaths = {"categoryCode"})
    @Query("SELECT c FROM EquCategory c WHERE c.equCategory.categoryCode IS NOT NULL ORDER BY c.categoryCode")
    Page<EquCategory> findAll(Pageable pageable);

    List<EquCategory> findByEquCategoryCategoryCodeIsNotNull();

    @Query("SELECT c from EquCategory c WHERE c.categoryCode != :categoryCode AND c.equCategory.categoryCode IS NOT NULL " +
            "ORDER BY c.categoryCode")
    List<EquCategory> findByCategoryName(Long categoryCode);
}
