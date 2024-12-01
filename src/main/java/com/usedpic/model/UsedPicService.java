package com.usedpic.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.used.model.UsedVO;

@Service("usedPicService")
public class UsedPicService {

	@Autowired
	UsedPicRepository repository;

	// 增
	public void addUsedPic(UsedPicVO usedPicVO) {
		repository.save(usedPicVO);
	}

	// 改
	public void updateUsedPic(UsedPicVO usedPicVO) {
		repository.save(usedPicVO);
	}

	// 刪除單一商品所有照片(預留)(依照usedNo)
	// 回傳旗標(boolean)做確認是否刪除
	// 若存在則刪除 不存在則回傳false
	public String deleteUsedPic(Integer usedNo) {
		try {
            if (repository.existsById(usedNo)) {
            	repository.deleteById(usedNo);
                return "true";// 資料成功刪除
            } else {
                return "false"; // 資料不存在
            }
        } catch (Exception e) {
        	return "Unexpected error while deleting usedPic.";
        }
	}

	// 刪單一張照片(依usedPicNo)
	// 回傳旗標(boolean)做確認是否刪除
	// 若存在則刪除 不存在則回傳false
	public  String deleteUsedPicById(Integer usedPicNo) {
		try {
            if (repository.existsById(usedPicNo)) {
            	repository.deleteById(usedPicNo);
                return "true";// 資料成功刪除
            } else {
                return "false"; // 資料不存在
            }
        } catch (Exception e) {
        	return "Unexpected error while deleting usedPic.";
        }
    
	}

	// 查單一照片
	public UsedPicVO findUsedPic(Integer usedPicNo) {
		Optional<UsedPicVO> optional = repository.findById(usedPicNo);
		return optional.orElse(null);
	}

	// 查詢單一商品的全部照片
	public List<UsedPicVO> findAllPicsByUsedNo(Integer usedNo) {

		return repository.findAllPicsByUsedNo(usedNo);
	}
}
