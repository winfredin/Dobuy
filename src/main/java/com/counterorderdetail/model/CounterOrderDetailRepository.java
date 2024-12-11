package com.counterorderdetail.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CounterOrderDetailRepository extends JpaRepository<CounterOrderDetailVO, Integer> {

   @Transactional
   @Modifying
   @Query(value = "DELETE FROM CounterOrderDetail WHERE counterOrderDetailNo = ?1", nativeQuery = true)
   void deleteByCounterOrderDetailNo(int counterOrderDetailNo);

   @Query(value = "FROM CounterOrderDetailVO WHERE counterOrderNo = ?1 AND goodsNo = ?2 AND goodsNum = ?3 ORDER BY counterOrderDetailNo")
   List<CounterOrderDetailVO> findByOthers(int counterOrderNo, int goodsNo, int goodsNum);
   
   @Transactional
   @Modifying
   @Query(value = "INSERT INTO counterorderdetail (goodsno, goodsnum, productprice, productdisprice, counterorderno) " +
                  "VALUES (:#{#details[0].goodsNo}, :#{#details[0].goodsNum}, :#{#details[0].productPrice}, :#{#details[0].productDisPrice}, :#{#details[0].counterOrderNo})",
          nativeQuery = true)
   void insertBatch(@Param("details") List<CounterOrderDetailVO> details);

   // 根據訂單編號查詢所有訂單明細
   List<CounterOrderDetailVO> findByCounterOrderNo(Integer counterOrderNo);
   
}
   
   
