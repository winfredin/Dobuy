package com.storecarousel.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("storeCarouselService")
public class storeCarouselService {

    @Autowired
    private storecarouselRepository repository;

//    @Autowired
//    private SessionFactory sessionFactory;

    // 新增輪播資訊
    public void addStoreCarousel(storeCarouselVO storeCarousel) {
        repository.save(storeCarousel);
    }

    // 更新輪播資訊
    public void updateStoreCarousel(storeCarouselVO storeCarousel) {
        repository.save(storeCarousel);
    }

    // 刪除輪播資訊
    public void deleteStoreCarousel(Integer storeCarouselNo) {
        if (repository.existsById(storeCarouselNo)) {
            repository.deleteById(storeCarouselNo);
        }
    }

    // 取得單筆輪播資訊
    public storeCarouselVO getOneStoreCarousel(Integer storeCarouselNo) {
        Optional<storeCarouselVO> optional = repository.findById(storeCarouselNo);
        return optional.orElse(null);
    }

    // 取得所有輪播資訊
    public List<storeCarouselVO> getAll(Map<Integer, Integer[]> map) {
        return repository.findAll();
    }

    // 根據櫃位編號取得所有輪播資訊
    public List<storeCarouselVO> getByCounterNo(Integer counterNo) {
        return repository.findByCounterNo(counterNo);
    }

    // 動態查詢輪播資訊
//    public List<storeCarouselVO> getAllByCriteria(Map<String, String[]> criteriaMap) {
//        try (Session session = sessionFactory.openSession()) {
//            return HibernateUtil_CompositeQuery_StoreCarousel.getAllC(criteriaMap, session);
//        }
//    }
}
