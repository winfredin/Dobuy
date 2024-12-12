package com.customerservice.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.faq.model.FaqVO;
import com.notice.model.NoticeVO;

public interface CustomerServiceRepository extends JpaRepository<CustomerServiceVO, Integer> {
	
	
	List<NoticeVO> findByMemNo(Integer memNo);

	

   
    
    
   
}

