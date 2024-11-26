package com.goodstype.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GoodsTypeRepository extends JpaRepository<GoodsTypeVO, Integer> {

    // ● 刪除商品類別根據goodstNo (自訂刪除方法)
    @Transactional
    @Modifying
    @Query(value = "delete from GoodsType where goodstNo = ?1", nativeQuery = true)
    void deleteByGoodstNo(int goodstNo);

    // ● (自訂)條件查詢：根據商品類別編號與商品名稱模糊查詢
    @Query(value = "from GoodsTypeVO where goodstNo = ?1 and goodstName like ?2 order by goodstNo")
    List<GoodsTypeVO> findByOthers(int goodstNo, String goodstName);
}
