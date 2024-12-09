package com.storecarousel.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("storeCarouselService")
public class StoreCarouselService {

    @Autowired
    private StorecarouselRepository repository;

//    @Autowired
//    private SessionFactory sessionFactory;

    // 新增輪播資訊
    public void addStoreCarousel(StoreCarouselVO storeCarousel) {
        getRepository().save(storeCarousel);
    }

    // 更新輪播資訊
    public void updateStoreCarousel(StoreCarouselVO storeCarousel) {
        getRepository().save(storeCarousel);
    }

    // 刪除輪播資訊
    public void deleteStoreCarousel(Integer storeCarouselNo) {
        if (getRepository().existsById(storeCarouselNo)) {
            getRepository().deleteById(storeCarouselNo);
        }
    }

    // 取得單筆輪播資訊
    public StoreCarouselVO getOneStoreCarousel(Integer storeCarouselNo) {
        Optional<StoreCarouselVO> optional = getRepository().findById(storeCarouselNo);
        return optional.orElse(null);
    }

    // 取得所有輪播資訊
    public List<StoreCarouselVO> getAll() {
        return getRepository().findAll();
    }

    // 根據櫃位編號取得所有輪播資訊
    public List<StoreCarouselVO> getByCounterNo(Integer counterNo) {
        return getRepository().findByCounterNo(counterNo);
    }
        
    // 其他方法保持不變，但需要考慮是否真的需要 map 參數
     public List<StoreCarouselVO> getAll(Integer counterNo) {  // 移除未使用的參數
        return getRepository().findAll();
        }

	public StorecarouselRepository getRepository() {
		return repository;
	}

	public void setRepository(StorecarouselRepository repository) {
		this.repository = repository;
	}


    // 動態查詢輪播資訊
//    public List<storeCarouselVO> getAllByCriteria(Map<String, String[]> criteriaMap) {
//        try (Session session = sessionFactory.openSession()) {
//            return HibernateUtil_CompositeQuery_StoreCarousel.getAllC(criteriaMap, session);
//        }
//    }
}
