// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.counterorder.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CounterOrderRepository extends JpaRepository<CounterOrderVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from counterorder where counterorderno =?1", nativeQuery = true)
	void deleteByCounterOrderNo(int counterOrderNo);

	//● (自訂)條件查詢
//	@Query(value = "from CounterOrderVO where counterorderno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<CounterOrderVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
}