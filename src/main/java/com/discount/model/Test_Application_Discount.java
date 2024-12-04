//package com.discount.model;
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
//import com.discount.controller.HibernateUtil_CompositeQuery_Discount;
//
//@SpringBootApplication
//public class Test_Application_Discount implements CommandLineRunner {
//
//    @Autowired
//    DiscountRepository repository;
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_Discount.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
////        DiscountVO discountVO1 = new DiscountVO();
////        discountVO1.setDisTitle("新年大促銷");
////        discountVO1.setDisContext("全館85折");
////        discountVO1.setDisRate(0.15);
////        discountVO1.setDisStatus(1);
////        discountVO1.setDescLimit("限量優惠");
////        discountVO1.setDisStart(java.sql.Timestamp.valueOf("2025-01-01 00:00:00"));
////        discountVO1.setDisEnd(java.sql.Timestamp.valueOf("2025-01-05 23:59:59"));
////        repository.save(discountVO1);
//
//        // ● 修改
////        DiscountVO discountVO2 = new DiscountVO();
////        discountVO2.setDisNo(3);
////        discountVO2.setDisTitle("VIP專屬優惠 - 更新版");
////        discountVO2.setDisContext("VIP會員享專屬折扣85折");
////        discountVO2.setDisRate(0.15);
////        discountVO2.setDisStatus(1);
////        discountVO2.setDescLimit("僅限VIP會員");
////        discountVO2.setDisStart(java.sql.Timestamp.valueOf("2024-12-01 00:00:00"));
////        discountVO2.setDisEnd(java.sql.Timestamp.valueOf("2025-01-01 23:59:59"));
////        repository.save(discountVO2);
//
//        // ● 刪除
////        repository.deleteByDisNo(5);
//
//        // ● 查詢-findByPrimaryKey
////        Optional<DiscountVO> optional = repository.findById(1);
////        if (optional.isPresent()) {
////            DiscountVO discountVO3 = optional.get();
////            System.out.print(discountVO3.getDisNo() + ", ");
////            System.out.print(discountVO3.getDisTitle() + ", ");
////            System.out.print(discountVO3.getDisContext() + ", ");
////            System.out.print(discountVO3.getDisRate() + ", ");
////            System.out.print(discountVO3.getDisStatus() + ", ");
////            System.out.print(discountVO3.getDescLimit() + ", ");
////            System.out.print(discountVO3.getDisStart() + ", ");
////            System.out.print(discountVO3.getDisEnd());
////            System.out.println();
////        }
//
//        // ● 查詢-getAll
////        List<DiscountVO> list = repository.findAll();
////        for (DiscountVO discount : list) {
////            System.out.print(discount.getDisNo() + ", ");
////            System.out.print(discount.getDisTitle() + ", ");
////            System.out.print(discount.getDisContext() + ", ");
////            System.out.print(discount.getDisRate() + ", ");
////            System.out.print(discount.getDisStatus() + ", ");
////            System.out.print(discount.getDescLimit() + ", ");
////            System.out.print(discount.getDisStart() + ", ");
////            System.out.print(discount.getDisEnd());
////            System.out.println();
////        }
//
//        // ● 複合查詢-getAll(map)
////        Map<String, String[]> map = new TreeMap<>();
////        map.put("disTitle", new String[] { "VIP" });
////        map.put("disRate", new String[] { "0.15" });
////        map.put("disStatus", new String[] { "1" });
////
////        List<DiscountVO> list2 = HibernateUtil_CompositeQuery_Discount.getAllD(map, sessionFactory.openSession());
////        for (DiscountVO discount : list2) {
////            System.out.print(discount.getDisNo() + ", ");
////            System.out.print(discount.getDisTitle() + ", ");
////            System.out.print(discount.getDisContext() + ", ");
////            System.out.print(discount.getDisRate() + ", ");
////            System.out.print(discount.getDisStatus() + ", ");
////            System.out.print(discount.getDescLimit() + ", ");
////            System.out.print(discount.getDisStart() + ", ");
////            System.out.print(discount.getDisEnd());
////            System.out.println();
////        }
//    }
//}
