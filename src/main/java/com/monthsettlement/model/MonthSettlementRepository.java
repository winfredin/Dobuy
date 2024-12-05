package com.monthsettlement.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MonthSettlementRepository extends JpaRepository<MonthSettlementVO, Integer> {

    // 刪除指定抽成月結編號
    @Transactional
    @Modifying
    @Query(value = "delete from monthSettlement where monthSettlementNo = ?1", nativeQuery = true)
    void deleteByMonthSettlementNo(int monthSettlementNo);

    // 自訂條件查詢
    @Query(value = "from MonthSettlementVO where monthSettlementNo = ?1 and counterNo = ?2 and month = ?3 order by monthSettlementNo")
    List<MonthSettlementVO> findByConditions(int monthSettlementNo, int counterNo, String month);
    
    
    
    
    
    
    
    
    
    
    
    
//    winfred====================================================================以下
    @Query("FROM MonthSettlementVO WHERE counterNo = :counterNo")
    List<MonthSettlementVO> findByCounterNo(Integer counterNo);
//    winfred====================================================================以上    
    
}

