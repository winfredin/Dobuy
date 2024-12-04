//package com.discount.controller;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import java.util.*;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 开始 取代原 org.hibernate.Criteria 介面
//import javax.persistence.criteria.Root;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.Query; //Hibernate 5 开始 取代原 org.hibernate.Query 介面
//
//import com.discount.model.DiscountVO;
//
//public class HibernateUtil_CompositeQuery_Discount {
//
//    // 动态生成条件
//    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<DiscountVO> root, String columnName, String value) {
//        Predicate predicate = null;
//
//        if ("disNo".equals(columnName)) {
//            // 用于 Integer 类型字段
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        } else if ("disRate".equals(columnName)) {
//            // 用于 Double 类型字段
//            predicate = builder.equal(root.get(columnName), Double.valueOf(value));
//        } else if ("createdAt".equals(columnName) || "updatedAt".equals(columnName) || "disStart".equals(columnName) || "disEnd".equals(columnName)) {
//            // 用于 Date 类型字段
//            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
//        } else if ("disTitle".equals(columnName) || "disContext".equals(columnName) || "descLimit".equals(columnName)) {
//            // 用于 String 类型字段
//            predicate = builder.like(root.get(columnName), "%" + value + "%");
//        } else if ("disStatus".equals(columnName)) {
//            // 用于 Integer/枚举类型字段
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        }
//
//        return predicate;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static List<DiscountVO> getAllD(Map<String, String[]> map, Session session) {
//        Transaction tx = session.beginTransaction();
//        List<DiscountVO> list = null;
//
//        try {
//            // 【●创建 CriteriaBuilder】
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            // 【●创建 CriteriaQuery】
//            CriteriaQuery<DiscountVO> criteriaQuery = builder.createQuery(DiscountVO.class);
//            // 【●创建 Root】
//            Root<DiscountVO> root = criteriaQuery.from(DiscountVO.class);
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
//                    System.out.println("有送出查询资料的字段数 count = " + count);
//                }
//            }
//
//            System.out.println("predicateList.size()=" + predicateList.size());
//            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
//            criteriaQuery.orderBy(builder.asc(root.get("disNo")));
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
