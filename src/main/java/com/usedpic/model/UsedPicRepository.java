package com.usedpic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;




public interface UsedPicRepository extends JpaRepository<UsedPicVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "delete from usedpic where usedNo =?1", nativeQuery = true)
	void deleteByUsedNo(Integer usedNo);
	
	
	
}
