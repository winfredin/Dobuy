package com.counter.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CounterRepository extends JpaRepository<CounterVO, Integer> {
    // 這裡可以根據需求定義額外的方法，例如根據櫃位帳號查找
    CounterVO findByCounterAccount(String counterAccount);
    
    // 檢查帳號是否重複
    boolean existsByCounterAccount(String counterAccount);
    
    // 檢查統一編號是否重複
    boolean existsByCounterUbn(String counterUbn);

    // 查找所有特定狀態的櫃位
    List<CounterVO> findByCounterStatus(Integer counterStatus);
    
    CounterVO findByCounterUbn(String counterUbn);
    
    //登入方法
    CounterVO findByCounterAccountAndCounterPassword(String counterAccount, String counterPassword);
    

    //修改櫃位狀態

   
    
    //以下昱夆新增 用courtNo去找countCName
    
    @Query(value = "SELECT counterCName FROM counter WHERE counterNo = ?1 ", nativeQuery = true)
	String getCounerCname(String counterNo);
    
    @Query(value = "SELECT counterNo FROM counter WHERE counterCname = ?1 ", nativeQuery = true)
    String getCounerNo(String counterCname);
    
    //以下昱夆新增

}
