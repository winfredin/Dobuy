package com.countercarousel.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monthsettlement.model.MonthSettlementVO;

@Service("carouseService")
public class CountercarouselService {
	@Autowired
	CountercarouselRepository countercarouselRepository;
	
	//--------------------定紘-----------------------
	
	@Autowired
    private CountercarouselRepository repository;
	
	//----------------------------------------------
	
	public List<CountercarouselVO> getPic(Integer counterNo){
		
		return countercarouselRepository.findNewest3(counterNo);
		
	}
	
	
	
	
	
	//---------------------定紘---------------------------------

	    
	    // 新增輪播資訊
	    public void addCounterCarousel(CountercarouselVO counterCarousel) {
	        repository.save(counterCarousel);
	    }

	    // 更新輪播資訊
	    public void updateCounterCarousel(CountercarouselVO counterCarousel) {
	    	if (counterCarousel.getCounterNo() == null) {
	            throw new IllegalArgumentException("counterNo cannot be null");
	        }
	    	System.out.println("goodsNo: " + counterCarousel.getGoodsNo());

	        repository.save(counterCarousel);
	    }

	    // 刪除輪播資訊
	    public void deleteCounterCarousel(Integer counterCarouselNo) {
	        if (repository.existsById(counterCarouselNo)) {
	            repository.deleteById(counterCarouselNo);
	        }
	    }

	    // 取得單筆輪播資訊
	    public CountercarouselVO getOneCounterCarousel(Integer counterCarouselNo) {
	        Optional<CountercarouselVO> optional = repository.findById(counterCarouselNo);
	        return optional.orElse(null);
	    }

	    // 取得所有輪播資訊
	    public List<CountercarouselVO> getAll() {
	        return repository.findAll();
	    }

	    // 根據櫃位編號取得所有輪播資訊
	    public List<CountercarouselVO> getByCounterNo(Integer counterNo) {
	        return repository.findByCounterNo(counterNo);
	    }
	
	    
}
