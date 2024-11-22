package com.usedpic.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.used.model.UsedVO;



@Service("usedPicService")
public class UsedPicService {
	
	@Autowired
	UsedPicRepository repository;
	//增
	public void addUsedPic(UsedPicVO usedPicVO) {
		repository.save(usedPicVO);
	}
	//改
	public void updateUsedPic(UsedPicVO usedPicVO) {
		repository.save(usedPicVO);
	}
	//刪
	public void deleteUsedPic(Integer usedNo) {
		if (repository.existsById(usedNo))
			repository.deleteByUsedNo(usedNo);
//		    repository.deleteById(usedPicNo);
	}
	//查單一照片
	public UsedPicVO findUsedPic(Integer usedPicNo) {
		Optional<UsedPicVO> optional =repository.findById(usedPicNo);
		return optional.orElse(null);
		
	}
}
