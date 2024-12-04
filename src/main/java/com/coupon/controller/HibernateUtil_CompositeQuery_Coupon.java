//package com.coupon.controller;
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
//import com.coupon.model.CouponVO;
//
//public class HibernateUtil_CompositeQuery_Coupon {
//
//    // 动态生成条件
//    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<CouponVO> root, String columnName, String value) {
//        Predicate predicate = null;
//
//        if ("couponNo".equals(columnName) || "counterNo".equals(columnName) || "usageLimit".equals(columnName)) {
//            // 用于 Integer 类型字段
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        } else if ("couponStatus".equals(columnName) || "checkStatus".equals(columnName)) {
//            // 用于 TINYINT 类型字段
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        } else if ("couponStart".equals(columnName) || "couponEnd".equals(columnName)) {
//            // 用于 TIMESTAMP 类型字段
//            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
//        } else if ("couponTitle".equals(columnName) || "couponContext".equals(columnName)) {
//            // 用于 String 类型字段
//            predicate = builder.like(root.get(columnName), "%" + value + "%");
//        }
//
//        return predicate;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static List<CouponVO> getAllC(Map<String, String[]> map, Session session) {
//        Transaction tx = session.beginTransaction();
//        List<CouponVO> list = null;
//
//        try {
//            // 【●创建 CriteriaBuilder】
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            // 【●创建 CriteriaQuery】
//            CriteriaQuery<CouponVO> criteriaQuery = builder.createQuery(CouponVO.class);
//            // 【●创建 Root】
//            Root<CouponVO> root = criteriaQuery.from(CouponVO.class);
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
//                    System.out.println("有送出查詢資料的欄位數 count = " + count);
//                }
//            }
//
//            System.out.println("predicateList.size()=" + predicateList.size());
//            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
//            criteriaQuery.orderBy(builder.asc(root.get("couponNo")));
//            // 【●最后完成创建 javax.persistence.Query●】
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
