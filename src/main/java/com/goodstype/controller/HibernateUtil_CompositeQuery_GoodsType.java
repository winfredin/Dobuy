package com.goodstype.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.goodstype.model.GoodsTypeVO;

public class HibernateUtil_CompositeQuery_GoodsType {

    // 根據查詢欄位名稱及值，動態生成Predicate
    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<GoodsTypeVO> root, String columnName, String value) {

        Predicate predicate = null;

        if ("goodstNo".equals(columnName)) // 用於 Integer
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        else if ("goodstName".equals(columnName)) // 用於 varchar
            predicate = builder.like(root.get(columnName), "%" + value + "%");

        return predicate;
    }

    // 根據條件查詢所有商品類別
    @SuppressWarnings("unchecked")
    public static List<GoodsTypeVO> getAllC(Map<String, String[]> map, Session session) {

        Transaction tx = session.beginTransaction();
        List<GoodsTypeVO> list = null;
        try {
            // 【●創建 CriteriaBuilder】
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 【●創建 CriteriaQuery】
            CriteriaQuery<GoodsTypeVO> criteriaQuery = builder.createQuery(GoodsTypeVO.class);
            // 【●創建 Root】
            Root<GoodsTypeVO> root = criteriaQuery.from(GoodsTypeVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

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

            // 組合查詢條件
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            // 依照商品類別編號升序排列
            criteriaQuery.orderBy(builder.asc(root.get("goodstNo")));

            // 【●最後完成創建 javax.persistence.Query】
            Query query = session.createQuery(criteriaQuery); // javax.persistence.Query; Hibernate 5 開始 取代原 org.hibernate.Query 介面
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex; // System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }
}

