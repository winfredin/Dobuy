package com.counter.model;

import com.counter.model.CounterVO;
import com.counter.model.CounterRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("counterService")
public class CounterService {

    @Autowired
    private CounterRepository repository;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private JavaMailSender mailSender;

    public void addCounter(CounterVO counterVO) {
        if (repository.existsByCounterAccount(counterVO.getCounterAccount())) {
            throw new IllegalArgumentException("櫃位帳號已存在，請使用其他帳號。");
        }
        repository.save(counterVO);
    }

    public void updateCounter(CounterVO counterVO) {
        repository.save(counterVO);
    }



    public CounterVO getOneCounter(Integer counterNo) {
        Optional<CounterVO> optional = repository.findById(counterNo);
        return optional.orElse(null);
    }
    
    public List<CounterVO> getAll(){
    	return repository.findAll();
    }

    public List<CounterVO> getAll(Map<String, String[]> map) {
        return repository.findAll();
    }
    
    public boolean isCounterUbnExists(String counterUbn) {
        return repository.existsByCounterUbn(counterUbn);
    }

    public boolean isCounterAccountExists(String counterAccount) {
        return repository.existsByCounterAccount(counterAccount);
    }
    public CounterVO authenticate(String counterAccount, String counterPassword) {
        return repository.findByCounterAccountAndCounterPassword(counterAccount, counterPassword);
    }
    
    //狀態修改 
    @Transactional
    public void upStatus(Integer counterNo, Integer counterStatus) {
        Optional<CounterVO> counterOpt = repository.findById(counterNo);
        if (counterOpt.isPresent()) {
            CounterVO counter = counterOpt.get();
            counter.setCounterStatus(counterStatus);
            repository.save(counter);

            // 檢查是否是停權狀態
            if (counterStatus == 0) {
                sendEmail(counter);
            }
        } else {
            // 處理錯誤情況
            throw new RuntimeException("櫃位不存在");
        }
    }

    private void sendEmail(CounterVO counter) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(counter.getCounterEmail());
        message.setSubject("您的櫃位已被停權");
        message.setText("親愛的 " + counter.getCounterName() + "，\n\n" +
                        "我們很遺憾地通知您，您的櫃位 " + counter.getCounterCName() + " 已被停權。請盡快與我們聯繫以解決問題。\n\n" +
                        "感謝您的理解。\n" +
                        "您的客服團隊");

        try {
            mailSender.send(message);
        } catch (MailException e) {
            // 處理發送郵件失敗的情況
            e.printStackTrace();
            // 這裡可以選擇記錄錯誤日志或拋出異常，但不要讓發送郵件的錯誤影響主業務邏輯
        }
    }

    
    //以下昱夆新增
    
    public String getCounterCnameByCounterNo(String counterNo) {
    	return repository.getCounerCname(counterNo);
    }
    
    public String getCounterNoByCounterCname(String counterCname) {
    	return repository.getCounerNo(counterCname);
    }
    
    
    
    //以上昱夆新增
}
