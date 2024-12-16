package com.customerservice.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.model.MsgVO;



@Service("customerServiceService")
public class CustomerServiceService {

    @Autowired
    CustomerServiceRepository repository;

    public void addComplaint(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }

    public void updateComplaint(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }
    
    public CustomerServiceVO getOneComplaint(Integer counterComplaintNo) {
        return repository.findById(counterComplaintNo).orElse(null);
    }

    public List<CustomerServiceVO> getAll() {
        return repository.findAll();
    }
    
    public void save(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }

    // 新增的方法
    public List<CustomerServiceVO> getOneCounterCustomerService(Integer counterNo) {
        return repository.findByCounterNo(counterNo);
    }
    
    
    //任國測試
    public int countPlaintUnread(Integer counterNo) {
    	return repository.counterPlaintReader(counterNo, (byte)0);
    }
}


