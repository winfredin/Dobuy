// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.manager.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ManagerRepository extends JpaRepository<ManagerVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from manager where managerno =?1", nativeQuery = true)
	void deleteByManagerNo(int managerNo);
	
	@Transactional
	@Query(value = "select * from manager where managerAccount =?1 AND managerPassword =?2 ", nativeQuery = true)
	ManagerVO findAP(String managerAccount,String managerPassword);

	@Transactional
	@Query(value = "select * from manager where managerAccount =?1" , nativeQuery = true)
	ManagerVO findAC(String managerAccount);
	
	
}