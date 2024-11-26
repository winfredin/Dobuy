package com.goodstype.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.goodstype.controller.HibernateUtil_CompositeQuery_GoodsType;

@SpringBootApplication
public class Test_Application_CommandLineRunner_GoodsType implements CommandLineRunner {

    @Autowired
    GoodsTypeRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public static void main(String[] args) {
        SpringApplication.run(Test_Application_CommandLineRunner_GoodsType.class);
    }

    @Override
    public void run(String... args) throws Exception {
        // ● 新增商品類別
//        GoodsTypeVO goodsTypeVO1 = new GoodsTypeVO();
//        goodsTypeVO1.setGoodstNo(11);  // 商品類別編號
//        goodsTypeVO1.setGoodstName("新商品類別1");  // 商品類別名稱
//        repository.save(goodsTypeVO1);

        // ● 修改商品類別
//        GoodsTypeVO goodsTypeVO2 = new GoodsTypeVO();
//        goodsTypeVO2.setGoodstNo(11);  // 假設要修改編號為2的商品類別
//        goodsTypeVO2.setGoodstName("修改後的商品類別");
//        repository.save(goodsTypeVO2);

        // ● 刪除商品類別
        // 假設刪除編號為11的商品類別
//        repository.deleteById(11);
        
        // ● 查詢單個商品類別 (根據primary key查詢)
//        Optional<GoodsTypeVO> optional = repository.findById(1);
//        if (optional.isPresent()) {
//            GoodsTypeVO goodsTypeVO3 = optional.get();
//            System.out.print("商品類別編號: " + goodsTypeVO3.getGoodstNo() + ", ");
//            System.out.println("商品類別名稱: " + goodsTypeVO3.getGoodstName());
//        } else {
//            System.out.println("找不到指定商品類別!");
//        }

        // ● 查詢所有商品類別
//        List<GoodsTypeVO> goodsTypeList = repository.findAll();
//        for (GoodsTypeVO goodsType : goodsTypeList) {
//            System.out.print("商品類別編號: " + goodsType.getGoodstNo() + ", ");
//            System.out.println("商品類別名稱: " + goodsType.getGoodstName());
//        }

        // ● 複合查詢（使用 Map 作為查詢條件）
//        Map<String, String[]> map = new TreeMap<String, String[]>();
//        map.put("goodstNo", new String[] { "1" });
//        map.put("goodstName", new String[] { "女士包包" });
//
//        List<GoodsTypeVO> list2 = HibernateUtil_CompositeQuery_GoodsType.getAllC(map, sessionFactory.openSession());
//        for (GoodsTypeVO goodsType : list2) {
//            System.out.print("商品類別編號: " + goodsType.getGoodstNo() + ", ");
//            System.out.println("商品類別名稱: " + goodsType.getGoodstName());
//        }

        // ● (自訂)條件查詢（例如根據商品類別名稱進行模糊查詢）
//        List<GoodsTypeVO> list3 = repository.findByOthers(1,"%包%");
//        if (!list3.isEmpty()) {
//            for (GoodsTypeVO goodsType : list3) {
//                System.out.print("商品類別編號: " + goodsType.getGoodstNo() + ", ");
//                System.out.println("商品類別名稱: " + goodsType.getGoodstName());
//            }
//        } else {
//            System.out.println("查無資料");
//        }
//        System.out.println("--------------------------------");
    }
}