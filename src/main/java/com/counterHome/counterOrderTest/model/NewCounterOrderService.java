package com.counterHome.counterOrderTest.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailRepository;

@Service("newCounterOrderService")
public class NewCounterOrderService {
	
	@Autowired
	NewCounterOrderRepository newCounterOrderRepository;

    @Autowired
    NewCounterOrderDetailRepository newCounterOrderDetailRepository;
    
    public NewCounterOrderVO savedOrder (NewCounterOrderVO newCounterOrderVO) {
    	return newCounterOrderRepository.save(newCounterOrderVO);
    }
}
