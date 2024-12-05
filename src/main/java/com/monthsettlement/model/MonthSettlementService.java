package com.monthsettlement.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monthsettlement.controller.HibernateUtil_CompositeQuery_MonthSettlement;

@Service("monthSettlementService")
public class MonthSettlementService {

    @Autowired
    private MonthSettlementRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增月結
    public void addMonthSettlement(MonthSettlementVO monthSettlement) {
        repository.save(monthSettlement);
    }

    // 更新月結
    public void updateMonthSettlement(MonthSettlementVO monthSettlement) {
        repository.save(monthSettlement);
    }

    // 刪除月結
    public void deleteMonthSettlement(Integer monthSettlementNo) {
        if (repository.existsById(monthSettlementNo)) {
            repository.deleteByMonthSettlementNo(monthSettlementNo);
        }
    }

    // 取得單筆月結
    public MonthSettlementVO getOneMonthSettlement(Integer monthSettlementNo) {
        Optional<MonthSettlementVO> optional = repository.findById(monthSettlementNo);
        return optional.orElse(null);
    }

    // 取得所有月結
    public List<MonthSettlementVO> getAll() {
        return repository.findAll();
    }

    // 動態查詢取得所有月結
    public List<MonthSettlementVO> getAll(Map<String, String[]> criteriaMap) {
        try (Session session = sessionFactory.openSession()) {
            return HibernateUtil_CompositeQuery_MonthSettlement.getAllC(criteriaMap, session);
        }
    }
    
//    public List<MonthSettlementVO> getOnemonthsettlement(Integer counterNo) {
////    	if (counterNo == null) {
////            return Collections.emptyList(); // 如果商家編號為空，返回空列表
////        }
//        return repository.getOnemonthsettlement(counterNo); // 如果不存在，返回 null
//    }

	public MonthSettlementVO findById(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<MonthSettlementVO> getOnemonthsettlement(Integer counterNo) {
	    // 查詢資料庫中的該櫃位的月結資料
	    return repository.findByCounterNo(counterNo); // 假設你有這樣的 DAO 方法
	}

}
