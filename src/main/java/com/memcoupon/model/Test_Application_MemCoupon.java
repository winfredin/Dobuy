//package com.memcoupon.model;
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
//import com.memcoupon.controller.HibernateUtil_CompositeQuery_MemCoupon;
//
//@SpringBootApplication
//public class Test_Application_MemCoupon implements CommandLineRunner {
//
//    @Autowired
//    MemCouponRepository repository;
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_MemCoupon.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
////        MemCouponVO memCouponVO1 = new MemCouponVO();
////        memCouponVO1.setMemNo(1001);
////        memCouponVO1.setCouponNo(1);
////        repository.save(memCouponVO1);
//
//        // ● 修改
////        MemCouponVO memCouponVO2 = new MemCouponVO();
////        memCouponVO2.setMemCouponNo(7);
////        memCouponVO2.setMemNo(1010);
////        memCouponVO2.setCouponNo(5);
////        repository.save(memCouponVO2);
//
//        // ● 刪除
////        repository.deleteByMemCouponNo(21);
//
//        // ● 查詢-findByPrimaryKey
////        Optional<MemCouponVO> optional = repository.findById(2);
////        if (optional.isPresent()) {
////            MemCouponVO memCouponVO3 = optional.get();
////            System.out.print(memCouponVO3.getMemCouponNo() + ", ");
////            System.out.print(memCouponVO3.getMemNo() + ", ");
////            System.out.print(memCouponVO3.getCouponNo());
////            System.out.println();
////        }
//
//        // ● 查詢-getAll
////        List<MemCouponVO> list = repository.findAll();
////        for (MemCouponVO aMemCoupon : list) {
////            System.out.print(aMemCoupon.getMemCouponNo() + ", ");
////            System.out.print(aMemCoupon.getMemNo() + ", ");
////            System.out.print(aMemCoupon.getCouponNo());
////            System.out.println();
////        }
//
//        // ● 複合查詢-getAll(map)
////        Map<String, String[]> map = new TreeMap<>();
////        map.put("memNo", new String[] { "1001" });
////        map.put("couponNo", new String[] { "1" });
//
////        List<MemCouponVO> list2 = HibernateUtil_CompositeQuery_MemCoupon.getAllC(map, sessionFactory.openSession());
////        for (MemCouponVO aMemCoupon : list2) {
////            System.out.print(aMemCoupon.getMemCouponNo() + ", ");
////            System.out.print(aMemCoupon.getMemNo() + ", ");
////            System.out.print(aMemCoupon.getCouponNo());
////            System.out.println();
////        }
//    }
//}
