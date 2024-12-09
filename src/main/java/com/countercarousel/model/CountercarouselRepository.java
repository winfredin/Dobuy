package com.countercarousel.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountercarouselRepository extends JpaRepository<CountercarouselVO, Integer>{
	@Query(value = "SELECT * FROM countercarousel where counterNo = ?1 ORDER BY carouselTime desc LIMIT 3", nativeQuery = true)
	List<CountercarouselVO> findNewest3(Integer counterNo);

//	List<CountercarouselVO> findByCounterNo(Integer counterNo);
	
	//-------------------定紘--------------------------------------
	
	
	@Query("SELECT c FROM CountercarouselVO c WHERE c.counterNo = :counterNo ORDER BY c.carouselTime DESC")
	List<CountercarouselVO> findByCounterNo1(@Param("counterNo") Integer counterNo);

	
//	    // 刪除指定輪播資訊編號
//	    @Query(value = "DELETE FROM countercarousel WHERE counterCarouselNo = ?1", nativeQuery = true)
//	    void deleteByCounterCarouselNo(Integer counterCarouselNo);

//	    // 根據櫃位編號查詢輪播資訊
//	    @Query("SELECT * FROM countercarousel WHERE counterNo = ? ORDER BY carouselTime DESC")
//	    List<CountercarouselVO> findByCounterNo1(Integer counterNo);

//	    // 根據商品編號查詢輪播資訊
//	    @Query("SELECT * FROM countercarousel WHERE goodsNo = :goodsNo ORDER BY carouselTime DESC")
//	    List<CountercarouselVO> findByGoodsNo(Integer goodsNo);
//
//	    // 根據輪播時間範圍查詢
//	    @Query("SELECT * FROM countercarousel WHERE carouselTime BETWEEN :startTime AND :endTime ORDER BY carouselTime DESC")
//	    List<CountercarouselVO> findByTimeRange(java.sql.Timestamp startTime, java.sql.Timestamp endTime);

//	    // 自訂條件查詢：櫃位編號 + 商品編號
//	    @Query("FROM countercarousel WHERE counterNo = :counterNo AND goodsNo = :goodsNo ORDER BY carouselTime DESC")
//	    List<CountercarouselVO> findByCounterNoAndGoodsNo(Integer counterNo, Integer goodsNo);
	
	    // 刪除指定輪播資訊編號
	    @Query(value = "DELETE FROM countercarousel WHERE counterCarouselNo = ?1", nativeQuery = true)
	    void deleteByCounterCarouselNo(Integer counterCarouselNo);

	    // 根據商品編號查詢輪播資訊
	    @Query("SELECT c FROM CountercarouselVO c WHERE c.goodsNo = :goodsNo ORDER BY c.carouselTime DESC")
	    List<CountercarouselVO> findByGoodsNo(@Param("goodsNo") Integer goodsNo);

	    // 根據輪播時間範圍查詢
	    @Query("SELECT c FROM CountercarouselVO c WHERE c.carouselTime BETWEEN :startTime AND :endTime ORDER BY c.carouselTime DESC")
	    List<CountercarouselVO> findByTimeRange(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);
	

	}


