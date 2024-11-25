package com.counterType.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.counter.model.CounterVO;

@Service("counterTypeService")
public class CounterTypeService {

    @Autowired
    CounterTypeRepository repository;

    public void addCounterType(CounterTypeVO counterTypeVO) {
        repository.save(counterTypeVO);
    }

    public void updateCounterType(CounterTypeVO counterTypeVO) {
        repository.save(counterTypeVO);
    }

//    public void deleteCounterType(Integer counterTypeNo) {
//        if (repository.existsById(counterTypeNo))
//            repository.deleteById(counterTypeNo);
//    }

    public CounterTypeVO getOneCounterType(Integer counterTypeNo) {
        Optional<CounterTypeVO> optional = repository.findById(counterTypeNo);
        return optional.orElse(null); // 如果值不存在，回傳 null
    }

    public List<CounterTypeVO> getAll() {
        return repository.findAll();
    }

    public Set<CounterVO> getCountersByCounterTypeNo(Integer counterTypeNo) {
        CounterTypeVO counterTypeVO = getOneCounterType(counterTypeNo);
        return counterTypeVO != null ? counterTypeVO.getCounters() : null;
    }
}
