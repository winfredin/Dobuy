package com.notice.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.model.MsgVO;

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
    
  //抓會員通知
    public List<NoticeVO> getOneMemberNotice(Integer memNo) {// 取得指定會員的通知
        return repository.findByMemNo(memNo); // 如果不存在，返回 null
    }
    
    public void markAllAsRead() {
        List<NoticeVO> notices = repository.findAll();
        for (NoticeVO notice : notices) {
            notice.setNoticeRead((byte)1);  // Assuming the noticeRead field is an integer and 1 represents 'read'
        }
        repository.saveAll(notices);
    }



    public void deleteAllByMemNo(Integer memNo) { // 刪除指定會員的所有通知
	}            
    public void markAllAsReadByMemNo(Integer memNo) {// 標記指定會員的所有通知為已讀
	}         


    



}




   


 

   
   