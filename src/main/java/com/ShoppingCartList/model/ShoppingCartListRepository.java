package com.ShoppingCartList.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import io.lettuce.core.dynamic.annotation.Param;

public interface ShoppingCartListRepository extends JpaRepository<ShoppingCartListVO, Integer> {

    // 刪除指定的購物車項目，根據 shoppingcartListNo 來進行刪除
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ShoppingCartList WHERE shoppingcartListNo = ?1", nativeQuery = true)
    void deleteByShoppingCartListNo(int shoppingcartListNo);

    // 根據會員編號查詢購物車中的所有商品
    @Query("FROM ShoppingCartListVO WHERE memNo = ?1 ORDER BY shoppingcartListNo")
    List<ShoppingCartListVO> findByMemNo(int memNo);

    // 根據商品編號查詢購物車中的所有紀錄
    @Query("FROM ShoppingCartListVO WHERE goodsNo = ?1 ORDER BY shoppingcartListNo")
    List<ShoppingCartListVO> findByGoodsNo(int goodsNo);

    // 根據會員編號和商品編號查詢指定商品在會員的購物車中的紀錄
    @Query("FROM ShoppingCartListVO WHERE memNo = ?1 AND goodsNo = ?2")
    List<ShoppingCartListVO> findByMemNoAndGoodsNo(int memNo, int goodsNo);
    
    
    @Query("SELECT s FROM ShoppingCartListVO s WHERE s.memNo = :memNo")
    List<ShoppingCartListVO> findByMemNo(@Param("memNo") Integer memNo);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ShoppingCartListVO s WHERE s.memNo = :memNo")
    void deleteByMemNo(@Param("memNo") Integer memNo);
}
