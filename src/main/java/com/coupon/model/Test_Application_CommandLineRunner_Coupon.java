
//package com.coupon.model;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.TreeMap;
//
//import javax.transaction.Transactional;
//
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.coupon.controller.HibernateUtil_CompositeQuery_Coupon;
//
//@SpringBootApplication
//public class Test_Application_CommandLineRunner_Coupon implements CommandLineRunner {
//
//    @Autowired
//    CouponRepository repository;
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_CommandLineRunner_Coupon.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
////        CouponVO couponVO1 = new CouponVO();
////        couponVO1.setCounterNo(2);
////        couponVO1.setCouponTitle("感恩節優惠");
////        couponVO1.setCouponContext("滿1000送100");
////        couponVO1.setCouponStart(java.sql.Timestamp.valueOf("2024-11-20 00:00:00"));
////        couponVO1.setCouponEnd(java.sql.Timestamp.valueOf("2024-11-30 23:59:59"));
////        couponVO1.setCouponStatus(1);
////        couponVO1.setUsageLimit(300);
////        couponVO1.setCheckStatus(1);
////        repository.save(couponVO1);
//
//        // ● 修改
////        CouponVO couponVO2 = new CouponVO();
////        couponVO2.setCouponNo(7);
////        couponVO2.setCounterNo(4);
////        couponVO2.setCouponTitle("修改後優惠");
////        couponVO2.setCouponContext("新內容描述");
////        couponVO2.setCouponStart(java.sql.Timestamp.valueOf("2024-12-01 00:00:00"));
////        couponVO2.setCouponEnd(java.sql.Timestamp.valueOf("2024-12-31 23:59:59"));
////        couponVO2.setCouponStatus(1);
////        couponVO2.setUsageLimit(400);
////        couponVO2.setCheckStatus(0);
////        repository.save(couponVO2);
//
//        // ● 刪除
////        repository.deleteById(10);
//
//        // ● 查詢-findByPrimaryKey
////        Optional<CouponVO> optional = repository.findById(3);
////        if (optional.isPresent()) {
////            CouponVO couponVO3 = optional.get();
////            System.out.print(couponVO3.getCouponNo() + ", ");
////            System.out.print(couponVO3.getCounterNo() + ", ");
////            System.out.print(couponVO3.getCouponTitle() + ", ");
////            System.out.print(couponVO3.getCouponContext() + ", ");
////            System.out.print(couponVO3.getCouponStart() + ", ");
////            System.out.print(couponVO3.getCouponEnd() + ", ");
////            System.out.print(couponVO3.getCouponStatus() + ", ");
////            System.out.print(couponVO3.getUsageLimit() + ", ");
////            System.out.print(couponVO3.getCheckStatus());
////            System.out.println();
////        }
//
//        // ● 查詢-getAll
////        List<CouponVO> list = repository.findAll();
////        for (CouponVO aCoupon : list) {
////            System.out.print(aCoupon.getCouponNo() + ", ");
////            System.out.print(aCoupon.getCounterNo() + ", ");
////            System.out.print(aCoupon.getCouponTitle() + ", ");
////            System.out.print(aCoupon.getCouponContext() + ", ");
////            System.out.print(aCoupon.getCouponStart() + ", ");
////            System.out.print(aCoupon.getCouponEnd() + ", ");
////            System.out.print(aCoupon.getCouponStatus() + ", ");
////            System.out.print(aCoupon.getUsageLimit() + ", ");
////            System.out.print(aCoupon.getCheckStatus());
////            System.out.println();
////        }
//
//        // ● 複合查詢-getAll(map)
////        Map<String, String[]> map = new TreeMap<>();
////        map.put("counterNo", new String[] { "2" });
////        map.put("couponStatus", new String[] { "1" });
////        map.put("checkStatus", new String[] { "1" });
//
////        List<CouponVO> list2 = HibernateUtil_CompositeQuery_Coupon.getAllC(map, sessionFactory.openSession());
////        for (CouponVO aCoupon : list2) {
////            System.out.print(aCoupon.getCouponNo() + ", ");
////            System.out.print(aCoupon.getCounterNo() + ", ");
////            System.out.print(aCoupon.getCouponTitle() + ", ");
////            System.out.print(aCoupon.getCouponContext() + ", ");
////            System.out.print(aCoupon.getCouponStart() + ", ");
////            System.out.print(aCoupon.getCouponEnd() + ", ");
////            System.out.print(aCoupon.getCouponStatus() + ", ");
////            System.out.print(aCoupon.getUsageLimit() + ", ");
////            System.out.print(aCoupon.getCheckStatus());
////            System.out.println();
////        }
//
//    }
//}

