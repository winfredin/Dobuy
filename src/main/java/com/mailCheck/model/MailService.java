package com.mailCheck.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailService {
	
	@Autowired
	MailRepository mailRepository;
	
	
	@Autowired
	@Qualifier("secondaryMailSender") // 指定使用 secondaryMailSender
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("TibameMailCIA103@gmail.com");
		message.setTo(toEmail);
		message.setSubject("驗證碼 (Secondary Email)");
		message.setText("您的驗證碼是：" + verificationCode + "，請在有效時間內使用！");
		mailSender.send(message);
	}
	
	
	public void insert(MailVO mailVO) {
		mailRepository.save(mailVO);
	}
	
	public List<MailVO> isMailChecked(String email) {
		return mailRepository.isMailChecked(email);
	}
	
	public MailVO getTheNewest(String mail) {
		return mailRepository.getTheNewest(mail);
	}
	
	public void updateStatus(MailVO mailVO) {
		mailRepository.save(mailVO);
	}
	
	@Transactional
	public void deleteUnverified(String email) {
		mailRepository.deleteUnverified(email);
	}

}
