package com.mailCheck.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MailRepository extends  JpaRepository<MailVO, Integer>{
	
	@Query(value = "SELECT * FROM emailverification WHERE email = ?1", nativeQuery = true)
	List<MailVO> isMailChecked(String email);
	
	@Query(value = "select * from emailverification where email=?1 order by sentTime desc limit 1;", nativeQuery = true)
	MailVO getTheNewest(String email);
	
	@Modifying
	@Query(value = "delete from emailverification where email=?1 and isVerified = 0 AND id > 0;", nativeQuery = true)
	void deleteUnverified(String email);
}
