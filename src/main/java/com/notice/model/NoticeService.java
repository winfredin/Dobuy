package com.notice.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("noticeService")
public class NoticeService {

    @Autowired
    NoticeRepository repository;

    public List<NoticeVO> getAll() {
        return repository.findAll();
    }

    public void save(NoticeVO noticeVO) {
        repository.save(noticeVO);
    }
    
    public NoticeVO getNoticeById(Integer noticeId) {
        return repository.findById(noticeId).orElse(null);
    }

    public void updateNotice(NoticeVO noticeVO) {
        repository.save(noticeVO);
    }
    
    public void deleteAll() {
    	repository.deleteAll();
    }
    
   
    



}




   


 

   
   