package com.ShoppingCartList.controller;

import java.util.*;

import javax.persistence.Query; // Hibernate 5 開始取代 org.hibernate.Query 介面
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ShoppingCartList.model.ShoppingCartListVO;

public class HibernateUtil_CompositeQuery_ShoppingCartList {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<ShoppingCartListVO> root, String columnName, String value) {
        Predicate predicate = null;

        // 根據不同欄位進行處理
        if ("shoppingcartListNo".equals(columnName)) // 用於 Integer
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        else if ("goodsPrice".equals(columnName) || "orderTotalprice".equals(columnName)) // 用於 Double
            predicate = builder.equal(root.get(columnName), Double.valueOf(value));
        else if ("goodsName".equals(columnName)) // 用於 varchar
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("memNo".equals(columnName)) { // 用於 Integer
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("goodsNo".equals(columnName)) { // 用於 Integer
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("counterNo".equals(columnName)) { // 用於 Integer
        	predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("goodsNum".equals(columnName)) { // 用於 Integer
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<ShoppingCartListVO> getAllC(Map<String, String[]> map, Session session) {

        // 開啟交易
        Transaction tx = session.beginTransaction();
        List<ShoppingCartListVO> list = null;

        try {
            // 創建 CriteriaBuilder
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 創建 CriteriaQuery
            CriteriaQuery<ShoppingCartListVO> criteriaQuery = builder.createQuery(ShoppingCartListVO.class);
            // 創建 Root
            Root<ShoppingCartListVO> root = criteriaQuery.from(ShoppingCartListVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

            Set<String> keys = map.keySet();
            int count = 0;

            // 遍歷所有查詢條件，並將符合條件的 predicate 加入條件列表
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }

            // 設置查詢條件
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            // 預設根據 shoppingcartListNo 排序
            criteriaQuery.orderBy(builder.asc(root.get("shoppingcartListNo")));

            // 創建查詢對象
            Query query = session.createQuery(criteriaQuery); // Hibernate 5 使用 javax.persistence.Query
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex; // 顯示錯誤訊息
        } finally {
            session.close(); // 關閉 session
        }

        return list;
    }
}
