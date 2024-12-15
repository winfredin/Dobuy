package com.counterHome.counterOrderDetailTest.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newCounterOrderDetailService")
public class NewCounterOrderDetailService {
	
	@Autowired
	NewCounterOrderDetailRepository newCounterOrderDetailRepository;
	
	public void saveOrderDetails(List<NewCounterOrderDetailVO> details) {
        // 使用 saveAll() 批量插入数据
		newCounterOrderDetailRepository.saveAll(details);
    }
	
}
