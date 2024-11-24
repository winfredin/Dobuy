package com.countercarousel.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("carouseService")
public class CountercarouselService {
	@Autowired
	CountercarouselRepository countercarouselRepository;
	
	
	public List<CountercarouselVO> getPic(){
		
		return countercarouselRepository.findNewest3();
		
	}
}
