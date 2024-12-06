package com.notice.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<NoticeVO, Integer> {
	
	@Query("SELECT COUNT(n) FROM NoticeVO n WHERE n.noticeRead = 0") 
	long countUnreadNotices();

	

    
}





    


    



    

