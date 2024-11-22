/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package com.coupondetail.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;

//import hibernate.util.HibernateUtil;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面

import com.coupondetail.model.CouponDetailVO;

public class HibernateUtil_CompositeQuery_CouponDetail {

    // 動態生成條件
    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<CouponDetailVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("couponDetailNo".equals(columnName) || "couponNo".equals(columnName) || "goodsNo".equals(columnName)) {
            // 用於 Integer 類型欄位
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("disRate".equals(columnName)) {
            // 用於 Double 類型欄位
            predicate = builder.equal(root.get(columnName), Double.valueOf(value));
        } else if ("createdAt".equals(columnName) || "updatedAt".equals(columnName)) {
            // 用於 Date 類型欄位
            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
        } else if ("counterContext".equals(columnName)) {
            // 用於 String 類型欄位
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<CouponDetailVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<CouponDetailVO> list = null;

        try {
            // 【●創建 CriteriaBuilder】
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 【●創建 CriteriaQuery】
            CriteriaQuery<CouponDetailVO> criteriaQuery = builder.createQuery(CouponDetailVO.class);
            // 【●創建 Root】
            Root<CouponDetailVO> root = criteriaQuery.from(CouponDetailVO.class);

            List<Predicate> predicateList = new ArrayList<>();

            Set<String> keys = map.keySet();
            int count = 0;
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }

            System.out.println("predicateList.size()=" + predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
            criteriaQuery.orderBy(builder.asc(root.get("couponDetailNo")));
            // 【●最後完成創建 javax.persistence.Query●】
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) 
            	tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return list;
    }
}
