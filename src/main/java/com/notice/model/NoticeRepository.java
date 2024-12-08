package com.notice.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.msg.model.MsgVO;

public interface NoticeRepository extends JpaRepository<NoticeVO, Integer> {
	
	@Query("SELECT COUNT(n) FROM NoticeVO n WHERE n.noticeRead = 0") 
	long countUnreadNotices();
	
	
	//抓會員通知
	@Query(value = "SELECT * FROM notice WHERE memNo = ?1 ", nativeQuery = true)
	List<NoticeVO> findByMemNo(Integer memNo);

	

    
}





    


    



    

