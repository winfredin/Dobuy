package com.used.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UsedRepository extends JpaRepository<UsedVO, Integer>{

//會員使用額外方法

//會員將商品從使用者介面刪除  v
	@Transactional
	@Modifying
	@Query(value = "UPDATE used SET usedState = 2 WHERE usedNo = ?1", nativeQuery = true)
	void deleteByUsedNo(Integer usedNo);

//賣家查詢自己的商品 v
	@Query(value ="SELECT * FROM used WHERE sellerNo = ?1 AND usedState!=2", nativeQuery = true)
	List<UsedVO> memberFindBySellerNo(Integer sellerNo);

//官方
//管理員找尋 二手賣家 所有商品 v
	@Query(value ="SELECT * FROM used WHERE sellerNo = ?1", nativeQuery = true)
	List<UsedVO> adminFindBySellerNo(Integer sellerNo);
	
//商城瀏覽 上架 商品 v
	@Query(value ="SELECT * FROM used WHERE  usedState=1", nativeQuery = true)
	List<UsedVO> selectAllUsedOnSale();
	
//商城瀏覽 特定類別 上架 商品 v
	@Query(value ="SELECT * FROM used WHERE  classNo=1? AND usedState=1", nativeQuery = true)
	List<UsedVO> selectClassUsedOnSale(Integer classNo);
	
//共同使用
//管理員(填單式)/賣家(下拉式選單)找尋二手賣家特定商品 v
	@Query(value ="SELECT * FROM used WHERE sellerNo = ?1 AND usedNo = ?2", nativeQuery = true)
	List<UsedVO> findBySellerNoAndUsedNo(Integer sellerNo, Integer usedNo);

	//訂單更改used商品的stock
	@Transactional
	@Modifying
	@Query(value = "UPDATE used SET usedStocks = ?1 WHERE usedNo = ?2 ", nativeQuery = true)
	void withholdingStock(Integer usedStocks,Integer usedNo);
}
