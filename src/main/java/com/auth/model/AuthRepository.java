// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.auth.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AuthRepository extends JpaRepository<AuthVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from auth where authNo =?1", nativeQuery = true)
	void deleteByAuthno(int authno);


}