package com.counter.model;

import com.counter.model.CounterVO;
import com.counter.model.CounterRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("counterService")
public class CounterService {

    @Autowired
    private CounterRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addCounter(CounterVO counterVO) {
        if (repository.existsByCounterAccount(counterVO.getCounterAccount())) {
            throw new IllegalArgumentException("櫃位帳號已存在，請使用其他帳號。");
        }
        repository.save(counterVO);
    }

    public void updateCounter(CounterVO counterVO) {
        repository.save(counterVO);
    }



    public CounterVO getOneCounter(Integer counterNo) {
        Optional<CounterVO> optional = repository.findById(counterNo);
        return optional.orElse(null);
    }
    
    public List<CounterVO> getAll(){
    	return repository.findAll();
    }

    public List<CounterVO> getAll(Map<String, String[]> map) {
        return repository.findAll();
    }
    
    public boolean isCounterUbnExists(String counterUbn) {
        return repository.existsByCounterUbn(counterUbn);
    }

    public boolean isCounterAccountExists(String counterAccount) {
        return repository.existsByCounterAccount(counterAccount);
    }
    public CounterVO authenticate(String counterAccount, String counterPassword) {
        return repository.findByCounterAccountAndCounterPassword(counterAccount, counterPassword);
    }
    
    //以下昱夆新增
    
    public String getCounterCnameByCounterNo(String counterNo) {
    	return repository.getCounerCname(counterNo);
    }
    
    public String getCounterNoByCounterCname(String counterCname) {
    	return repository.getCounerNo(counterCname);
    }
    
    
    
    //以上昱夆新增
}
