package com.discount.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DiscountRepository extends JpaRepository<DiscountVO, Integer> {

    // 使用原生 SQL 删除平台优惠记录
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Discount WHERE disNo = ?1", nativeQuery = true)
    void deleteByDisNo(int disNo);

    // 自定义条件查询
    @Query(value = "FROM DiscountVO WHERE disTitle = ?1 AND disRate = ?2 AND disStatus = ?3 ORDER BY disNo")
    List<DiscountVO> findByOthers(String disTitle, double disRate, int disStatus);
}
