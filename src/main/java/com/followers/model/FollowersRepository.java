package com.followers.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.monthsettlement.model.MonthSettlementVO;

public interface FollowersRepository extends JpaRepository<FollowersVO, Integer> {

    // 使用原生 SQL 删除追蹤清單记录
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Followers WHERE trackListNo = ?1", nativeQuery = true)
    void deleteByTrackListNo(int trackListNo);

    // 自定义条件查询
    @Query(value = "FROM FollowersVO WHERE followersNo = ?1 AND counterNo = ?2 ORDER BY trackListNo")
    List<FollowersVO> findByFollowersNoAndCounterNo(int followersNo, int counterNo);
    
    @Query("FROM FollowersVO WHERE counterNo = :counterNo")
    List<FollowersVO> findByCounterNo(Integer counterNo);
}
