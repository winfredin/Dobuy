package com.faq.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("faqService")
public class FaqService {
	@Autowired
	FaqRepository faqRepository;
	
	public void addFaq(FaqVO faqVO) {
		faqRepository.save(faqVO);
    }
	
	public List<FaqVO> getOneCounterFaq(Integer counterNo) {
		return faqRepository.findByCounterNo(counterNo); // 如果不存在，返回 null
	}
	
	public FaqVO getOneFaq(String faqNo) {
		return faqRepository.findByFaqNo(faqNo); // 如果不存在，返回 null
	}
	
	public void updateFaq(FaqVO faqVO) {
		faqRepository.save(faqVO);
	}
	
	public void deleteFaq(Integer faqNo) {
		faqRepository.deleteById(faqNo);
	}

}
