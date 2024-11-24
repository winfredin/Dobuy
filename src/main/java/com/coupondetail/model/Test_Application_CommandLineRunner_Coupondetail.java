package com.coupondetail.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.coupondetail.controller.HibernateUtil_CompositeQuery_CouponDetail;

@SpringBootApplication
public class Test_Application_CommandLineRunner_Coupondetail implements CommandLineRunner {

    @Autowired
    CouponDetailRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public static void main(String[] args) {
        SpringApplication.run(Test_Application_CommandLineRunner_Coupondetail.class);
    }

    
    @Override
    public void run(String... args) throws Exception {

        // ● 新增
//        CouponDetailVO couponDetailVO1 = new CouponDetailVO();
//        couponDetailVO1.setCouponNo(101);
//        couponDetailVO1.setGoodsNo(101);
//        couponDetailVO1.setCreatedAt(java.sql.Timestamp.valueOf("2024-11-01 10:00:00"));
//        couponDetailVO1.setUpdatedAt(java.sql.Timestamp.valueOf("2024-11-01 11:00:00"));
//        couponDetailVO1.setCounterContext("滿額折扣");
//        couponDetailVO1.setDisRate(0.15);
//        repository.save(couponDetailVO1);

        // ● 修改
//        CouponDetailVO couponDetailVO2 = new CouponDetailVO();
//        couponDetailVO2.setCouponDetailNo(7);
//        couponDetailVO2.setCouponNo(60);
//        couponDetailVO2.setGoodsNo(100);
//        couponDetailVO2.setCreatedAt(java.sql.Timestamp.valueOf("2024-11-02 10:00:00"));
//        couponDetailVO2.setUpdatedAt(java.sql.Timestamp.valueOf("2024-11-02 12:00:00"));
//        couponDetailVO2.setCounterContext("優惠");
//        couponDetailVO2.setDisRate(0.20);
//        repository.save(couponDetailVO2);
        

        // ● 刪除
//        repository.deleteByCouponDetailNo(21);

        // ● 查詢-findByPrimaryKey
//        Optional<CouponDetailVO> optional = repository.findById(2);
//        if (optional.isPresent()) {
//            CouponDetailVO couponDetailVO3 = optional.get();
//            System.out.print(couponDetailVO3.getCouponDetailNo() + ", ");
//            System.out.print(couponDetailVO3.getCouponNo() + ", ");
//            System.out.print(couponDetailVO3.getGoodsNo() + ", ");
//            System.out.print(couponDetailVO3.getCreatedAt() + ", ");
//            System.out.print(couponDetailVO3.getUpdatedAt() + ", ");
//            System.out.print(couponDetailVO3.getCounterContext() + ", ");
//            System.out.print(couponDetailVO3.getDisRate());
//            System.out.println();
//        }

        // ● 查詢-getAll
//        List<CouponDetailVO> list = repository.findAll();
//        for (CouponDetailVO aCoupon : list) {
//            System.out.print(aCoupon.getCouponDetailNo() + ", ");
//            System.out.print(aCoupon.getCouponNo() + ", ");
//            System.out.print(aCoupon.getGoodsNo() + ", ");
//            System.out.print(aCoupon.getCreatedAt() + ", ");
//            System.out.print(aCoupon.getUpdatedAt() + ", ");
//            System.out.print(aCoupon.getCounterContext() + ", ");
//            System.out.print(aCoupon.getDisRate());
//            System.out.println();
//        }

        // ● 複合查詢-getAll(map)
//        Map<String, String[]> map = new TreeMap<>();
//        map.put("couponNo", new String[] { "2" });
//        map.put("goodsNo", new String[] { "102" });
//        map.put("disRate", new String[] { "0.20" });

//        List<CouponDetailVO> list2 = HibernateUtil_CompositeQuery_CouponDetail.getAllC(map, sessionFactory.openSession());
//        for (CouponDetailVO aCoupon : list2) {
//            System.out.print(aCoupon.getCouponDetailNo() + ", ");
//            System.out.print(aCoupon.getCouponNo() + ", ");
//            System.out.print(aCoupon.getGoodsNo() + ", ");
//            System.out.print(aCoupon.getCreatedAt() + ", ");
//            System.out.print(aCoupon.getUpdatedAt() + ", ");
//            System.out.print(aCoupon.getCounterContext() + ", ");
//            System.out.print(aCoupon.getDisRate());
//            System.out.println();
//        }
        
        
    }
}
