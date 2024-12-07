package com.storecarousel.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StorecarouselRepository extends JpaRepository<StoreCarouselVO, Integer> {

    // 刪除指定輪播資訊編號
    @Query(value = "DELETE FROM StoreCarousel WHERE storeCarouselNo = ?1", nativeQuery = true)
    void deleteByStoreCarouselNo(Integer storeCarouselNo);

    // 根據櫃位編號查詢輪播資訊
    @Query("FROM StoreCarouselVO WHERE counterNo = :counterNo ORDER BY carouselTime DESC")
    List<StoreCarouselVO> findByCounterNo(Integer counterNo);

    // 根據優惠編號查詢輪播資訊
    @Query("FROM StoreCarouselVO WHERE disNo = :disNo ORDER BY carouselTime DESC")
    List<StoreCarouselVO> findByDisNo(Integer disNo);

    // 根據輪播時間範圍查詢
    @Query("FROM StoreCarouselVO WHERE carouselTime BETWEEN :startTime AND :endTime ORDER BY carouselTime DESC")
    List<StoreCarouselVO> findByTimeRange(java.sql.Timestamp startTime, java.sql.Timestamp endTime);

    // 自訂條件查詢：櫃位編號 + 優惠編號
    @Query("FROM StoreCarouselVO WHERE counterNo = :counterNo AND disNo = :disNo ORDER BY carouselTime DESC")
    List<StoreCarouselVO> findByCounterNoAndDisNo(Integer counterNo, Integer disNo);
}
