package com.customerservice.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faq.model.FaqVO;

@Service("customerServiceService")
public class CustomerServiceService {

    @Autowired
    CustomerServiceRepository repository;

    public void addMsg(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }

    public void updateMsg(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }
    

    public List<CustomerServiceVO> getAll() {
        return repository.findAll();
    }
    
    public void save(CustomerServiceVO customerServiceVO) {
        repository.save(customerServiceVO);
    }
}
