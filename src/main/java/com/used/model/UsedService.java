package com.used.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usedpic.model.UsedPicRepository;
import com.usedpic.model.UsedPicVO;



@Service("usedService")
public class UsedService {
	
	@Autowired
	UsedRepository repository;
	
	@Autowired
	UsedPicRepository usedPicRepository;
	
	//增
	@Transactional
	public UsedVO addUsed(UsedVO usedVO) {
		
		return repository.save(usedVO);
	}
	@Transactional
	public UsedVO saveUsedWithPics(UsedVO usedVO, List<UsedPicVO> usedPicsList) {
		UsedVO savedUsed =repository.save(usedVO);
		List<UsedPicVO> usedPicWithUsedNo = new ArrayList<>();
		for(UsedPicVO usedPicVO:usedPicsList) {
			usedPicVO.setUsedVO(savedUsed);
			usedPicWithUsedNo.add(usedPicVO);
		}
		List<UsedPicVO> savedUsedPic=usedPicRepository.saveAll(usedPicWithUsedNo);
	    
		 savedUsed.setUsedPics(savedUsedPic);
		 
		 return savedUsed;
	}
	
	
	
	//改
	@Transactional
	public Integer updateUsed(UsedVO usedVO) {
		
		UsedVO savedUsed =repository.save(usedVO);
		
		return savedUsed.getUsedNo();
	}
	
	//刪 會員將商品從使用者介面刪除 
	@Transactional
	public void deleteUsed(Integer usedNo) {
		
		repository.deleteByUsedNo(usedNo);
		
	}
	//官方搜尋單一商品(包含未上架以及被使用者從介面刪除(usedState=2))
	public UsedVO getOneUsed(Integer usedNo) {
		Optional<UsedVO> optional = repository.findById(usedNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	
	//買家搜尋自己的商品(不包含已從介面刪除之商品) repository 20 row
	public List<UsedVO> memberSelectBySellerNo(Integer sellerNo) {
		
		return repository.memberFindBySellerNo(sellerNo);
		
	}
	
	//官方/管理員搜尋賣家所有商品  repository 25 row 
	public List<UsedVO> adminSelectBySellerNo(Integer sellerNo) {
		
		return repository.adminFindBySellerNo(sellerNo);
		
	}
	//官方/商城搜尋所有上架商品 repository 29 row
	public List<UsedVO> selectAllUsedOnSale() {
		
		return repository.selectAllUsedOnSale();
		
	}
	//官方/商城搜尋特定類別上架商品  repository 33 row
	public List<UsedVO> selectClassUsedOnSale(Integer classNo) {
		
		return repository.selectClassUsedOnSale(classNo);
		
	}
	
	
	//管理員(填單式)/賣家(下拉式選單)找尋二手賣家特定商品 repository 39 row
	public List<UsedVO> adminSelectBySellerNoAndUsedNo(Integer sellerNo, Integer usedNo) {
		
		return repository.findBySellerNoAndUsedNo(sellerNo, usedNo);
		
	}
	
	//官方/管理員搜尋所有二手商品資料(包含未上架以及被使用者從介面刪除(usedState=2))
	public List<UsedVO> getAll() {
		
		return repository.findAll();
		
	}
	
	//訂單更改used商品的stock
	public void withholdingStock(Integer usedStocks,Integer usedNo) {
		
		repository.withholdingStock( usedStocks, usedNo);
		
	}
	
	
	
}
