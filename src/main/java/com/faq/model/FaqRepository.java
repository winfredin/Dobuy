package com.faq.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<FaqVO, Integer> {

	@Query(value = "SELECT * FROM faq WHERE counterNo = ?1 ", nativeQuery = true)
	List<FaqVO> findByCounterNo(Integer counterNo);
	
	@Query(value = "SELECT * FROM faq WHERE faqNo = ?1 ", nativeQuery = true)
	FaqVO findByFaqNo(String FaqNo);

}
