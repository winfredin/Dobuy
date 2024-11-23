package hibernate.util.CompositeQuery;

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

import com.goods.model.GoodsVO;


public class HibernateUtil_CompositeQuery_Goods {

	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<GoodsVO> root, String columnName, String value) {
	    Predicate predicate = null;

	    if (value == null || value.trim().isEmpty()) {
	        // 如果 value 為 null 或空字符串，直接返回 null，不生成條件
	        return null;
	    }

	    // 根據欄位類型創建對應的 Predicate
	    if ("goodsNo".equals(columnName)) { // 用於 Integer
	        try {
	            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid value for goodsNo: " + value);
	        }
	    } else if ("goodsPrice".equals(columnName) || "goodsAmount".equals(columnName)) { // 用於 Integer
	        try {
	            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid value for goodsPrice or goodsAmount: " + value);
	        }
	    } else if ("goodsName".equals(columnName) || "goodsDescription".equals(columnName)) { // 用於 varchar
	        predicate = builder.like(root.get(columnName), "%" + value + "%");
	    } else if ("goodsStatus".equals(columnName)) { // 用於 TinyInt
	        try {
	            predicate = builder.equal(root.get(columnName), Byte.valueOf(value));
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid value for goodsStatus: " + value);
	        }
	    } else if ("goodsDate".equals(columnName) || "goodsEnddate".equals(columnName)) { // 用於 DateTime
	        try {
	            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid value for goodsDate or goodsEnddate: " + value);
	        }
	    }

	    return predicate;
	}

	@SuppressWarnings("unchecked")
	public static List<GoodsVO> getAllC(Map<String, String[]> map, Session session) {
	    Transaction tx = session.beginTransaction();
	    List<GoodsVO> list = null;
	    try {
	        // 【●創建 CriteriaBuilder】
	        CriteriaBuilder builder = session.getCriteriaBuilder();
	        // 【●創建 CriteriaQuery】
	        CriteriaQuery<GoodsVO> criteriaQuery = builder.createQuery(GoodsVO.class);
	        // 【●創建 Root】
	        Root<GoodsVO> root = criteriaQuery.from(GoodsVO.class);

	        List<Predicate> predicateList = new ArrayList<>();
	        
	        Set<String> keys = map.keySet();
	        int count = 0;
	        for (String key : keys) {
	            String value = map.get(key)[0];
	            if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
	                Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
	                if (predicate != null) { // 確保 Predicate 不為 null
	                    predicateList.add(predicate);
	                    count++;
	                }
	            }
	        }

	        System.out.println("有送出查詢資料的欄位數 count = " + count);
	        System.out.println("predicateList.size()=" + predicateList.size());
	        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
	        criteriaQuery.orderBy(builder.asc(root.get("goodsNo")));

	        // 【●最後完成創建 javax.persistence.Query】
	        Query query = session.createQuery(criteriaQuery);
	        list = query.getResultList();

	        tx.commit();
	    } catch (RuntimeException ex) {
	        if (tx != null) tx.rollback();
	        throw ex;
	    } finally {
	        session.close();
	    }

	    return list;
	}

}
