package com.usedpic.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.used.model.UsedVO;




public interface UsedPicRepository extends JpaRepository<UsedPicVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "delete from usedpic where usedNo =?1", nativeQuery = true)
	void deleteByUsedNo(Integer usedNo);
	
	
	//回傳檢驗是否為list.size()==0
	@Query(value = "select * from usedpic where usedNo =?1", nativeQuery = true)
	List<UsedPicVO> findAllPicsByUsedNo(Integer usedNo);
	
}
