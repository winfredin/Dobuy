package com.mailCheck.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

	public void sendHtmlEmail(String toEmail, String verificationCode) {
		// 创建 MimeMessage 对象
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			// 使用 MimeMessageHelper 设置邮件内容
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setFrom("TibameMailCIA103@gmail.com");
			helper.setTo(toEmail);
			helper.setSubject("HTML 郵件");

			// HTML 内容直接写在代码中
			String htmlContent = """
					    <!DOCTYPE html>
					<html>
					<head>
					    <meta charset="UTF-8">
					    <title>DOBUY Mail</title>
					</head>
					<body>
					    <img src="cid:logoImage" alt="Logo" class="logo" style="width: 150px; height: 150px;">
					    <p>親愛的貴賓，您好</p>
					    <p>您的驗證碼是：<strong>%s</strong></p>
					    <p>請在有效時間內使用！</p>
					</body>
					</html>
					    """;
			
			 // 动态替换占位符
	        htmlContent = String.format(htmlContent, verificationCode);
			
			// 设置 HTML 内容为邮件正文
			helper.setText(htmlContent, true); // 第二个参数 true 表示启用 HTML 格式

			// 嵌入图片
			ClassPathResource imageResource = new ClassPathResource("static/counterPage/img/core-img/logo-noback.png");
			helper.addInline("logoImage", imageResource); // "logoImage" 对应 HTML 文件中的 cid:logoImage

			// 发送邮件
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace(); // 记录异常信息
		}
	}

}
