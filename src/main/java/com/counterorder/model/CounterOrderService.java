package com.counterorder.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service("CounterOrderService")
public class CounterOrderService {

	@Autowired
	CounterOrderRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addCounterOrder(CounterOrderVO counterOrderVO) {
		repository.save(counterOrderVO);
	}

	public void updateCounterOrder(CounterOrderVO counterOrderVO) {
		repository.save(counterOrderVO);
	}

	public void deleteCounterOrder(Integer counterOrderNo) {
		if (repository.existsById(counterOrderNo))
			repository.deleteByCounterOrderNo(counterOrderNo);
//		    repository.deleteById(empno);
	}

	public CounterOrderVO getOneCounterOrder(Integer counterOrderNo) {
		Optional<CounterOrderVO> optional = repository.findById(counterOrderNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<CounterOrderVO> getAll() {
		return repository.findAll();
	}
	public Integer getone(Integer memno) {
		return repository.findone(memno);
	}
	
//	結帳用
	public CounterOrderVO createOrder(Integer memNo, Integer counterNo, int totalBefore, int totalAfter) {
		// TODO Auto-generated method stub
		return null;
	}

}