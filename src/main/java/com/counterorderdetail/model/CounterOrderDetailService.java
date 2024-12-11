package com.counterorderdetail.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("counterOrderDetailService")
public class CounterOrderDetailService {
	         
   @Autowired
   CounterOrderDetailRepository repository;
   
   @Autowired
   private SessionFactory sessionFactory;

   public void addCounterOrderDetail(CounterOrderDetailVO counterOrderDetailVO) {
       repository.save(counterOrderDetailVO);
   }

   public void updateCounterOrderDetail(CounterOrderDetailVO counterOrderDetailVO) {
       repository.save(counterOrderDetailVO);
   }

   public void deleteCounterOrderDetail(Integer counterOrderDetailNo) {
       if (repository.existsById(counterOrderDetailNo))
           repository.deleteByCounterOrderDetailNo(counterOrderDetailNo);
   }

   public CounterOrderDetailVO getOneCounterOrderDetail(Integer counterOrderDetailNo) {
       Optional<CounterOrderDetailVO> optional = repository.findById(counterOrderDetailNo);
       return optional.orElse(null);
   }

   public List<CounterOrderDetailVO> getAll() {
       return repository.findAll();
   }
   public void addCounterOrderDetails(List<CounterOrderDetailVO> details) {
	   repository.insertBatch(details);
	}
}