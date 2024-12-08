package com.usedorder.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.model.MsgVO;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;

@Service("usedOrderService")
public class UsedOrderService {

    @Autowired
    UsedOrderRepository repository;
    
    @Autowired
    private SessionFactory sessionFactory;

    public void addUsedOrder(UsedOrderVO usedOrderVO) {
        repository.save(usedOrderVO);
    }

    public void updateUsedOrder(UsedOrderVO usedOrderVO) {
        repository.save(usedOrderVO);
    }

    public UsedOrderVO getOneUsedOrder(Integer usedOrderNo) {
        Optional<UsedOrderVO> optional = repository.findById(usedOrderNo);
        return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }

    public List<UsedOrderVO> getAll() {
        return repository.findAll();
    }

    // public List<UsedOrder> getAll(Map<String, String[]> map) {
    //     return HibernateUtil_CompositeQuery_Emp3.getAllC(map, sessionFactory.openSession());
    // }
    
    public UsedOrderVO getOrderById(Integer orderNo) {
        return repository.findById(orderNo).orElse(null);
    }

    public void updateOrder(UsedOrderVO usedOrder) {
        repository.save(usedOrder);
    }
    
    public void save(UsedOrderVO usedOrderVO) {
        repository.save(usedOrderVO);
    }
    
    //訂單更改狀態用   gary lee
    public int changeStatusByUsedOrderNo(Byte deliveryStatus,Integer usedOrderNo) {
    	try {
    	repository.changeStatusByUsedOrderNo(deliveryStatus, usedOrderNo);
    	
    		return 1;
    	}catch(Exception e){
    		return 0;
    	}
    }
    
    //預存訂單資料且回傳訂單編號用
    public Integer addUsedOrderwhileCheckOut(UsedOrderVO usedOrderVO) {
    	
    	 usedOrderVO = repository.save(usedOrderVO);
    	 
        return usedOrderVO.getUsedOrderNo();
    }

    
    
}




   





   



   

    


    

    



