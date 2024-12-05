package com.usedorder.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UsedOrderRepository extends JpaRepository<UsedOrderVO, Integer> {

//    @Transactional
//    @Modifying
//    @Query(value = "delete from counterinform where usedOrderNo = ?1", nativeQuery = true)
//    void deleteByUsedOrderNo(int usedOrderNo);

}