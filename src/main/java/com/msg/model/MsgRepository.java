package com.msg.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MsgRepository extends JpaRepository<MsgVO, Integer> {

	

    @Transactional
    @Modifying
    @Query(value = "delete from counterinform where counterInformNo = ?1", nativeQuery = true)
    void deleteByCounterInformNo(int counterInformNo);
}



