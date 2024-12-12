package com.counterorder.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.counterorderdetail.model.CounterOrderDetailVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

@Service
public class InventoryRestoreTask {

    @Autowired
    private CounterOrderService counterOrderService;
    
    @Autowired
    GoodsService goodsService;
    
    @Scheduled(fixedRate = 60000)
    public void runScheduledTask() {
    	counterOrderService.restore();
    }
    
    
    
}