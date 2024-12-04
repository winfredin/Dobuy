///*
// *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
// *  2. 為了避免影響效能:
// *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
// * */
//
//package com.memcoupon.controller;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import java.util.*;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.Query;
//
//import com.memcoupon.model.MemCouponVO;
//
//public class HibernateUtil_CompositeQuery_MemCoupon {
//
//    // 動態生成條件
//    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<MemCouponVO> root, String columnName, String value) {
//        Predicate predicate = null;
//
//        if ("memCouponNo".equals(columnName) || "memNo".equals(columnName) || "couponNo".equals(columnName)) {
//            // 用於 Integer 類型欄位
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        }
//
//        return predicate;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static List<MemCouponVO> getAllC(Map<String, String[]> map, Session session) {
//        Transaction tx = session.beginTransaction();
//        List<MemCouponVO> list = null;
//
//        try {
//            // 【●創建 CriteriaBuilder】
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            // 【●創建 CriteriaQuery】
//            CriteriaQuery<MemCouponVO> criteriaQuery = builder.createQuery(MemCouponVO.class);
//            // 【●創建 Root】
//            Root<MemCouponVO> root = criteriaQuery.from(MemCouponVO.class);
//
//            List<Predicate> predicateList = new ArrayList<>();
//
//            Set<String> keys = map.keySet();
//            int count = 0;
//            for (String key : keys) {
//                String value = map.get(key)[0];
//                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
//                    count++;
//                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
//                    System.out.println("有送出查詢資料的欄位數count = " + count);
//                }
//            }
//
//            System.out.println("predicateList.size()=" + predicateList.size());
//            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
//            criteriaQuery.orderBy(builder.asc(root.get("memCouponNo")));
//            // 【●最後完成創建 javax.persistence.Query●】
//            Query query = session.createQuery(criteriaQuery);
//            list = query.getResultList();
//
//            tx.commit();
//        } catch (RuntimeException ex) {
//            if (tx != null) 
//                tx.rollback();
//            throw ex;
//        } finally {
//            session.close();
//        }
//
//        return list;
//    }
//}
