package com.counter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.nio.file.Files;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.counter.model.CounterService;


@Controller
@RequestMapping("/counter")
public class CounterDBGifReaderController {
	
	@Autowired
	CounterService counterSvc;
	
	/*
	 * This method will serve as listOneEmp.html , listAllEmp.html handler.
	 */
	@GetMapping("DBGifReader")
	public void dBGifReader(@RequestParam("counterNo") String counterNo, HttpServletRequest req, HttpServletResponse res)
			                                                                                          throws IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();

		try {
//			EmpService empSvc = new EmpService();
			out.write(counterSvc.getOneCounter(Integer.valueOf(counterNo)).getCounterPic());
		} catch (Exception e) {
			// 指定圖片的路徑
			String imagePath = this.getClass().getClassLoader().getResource("static/images/noHand.png").getPath(); // 請替換為你的圖片路徑
            File imageFile = new File(imagePath);

            // 讀取圖片的位元組數據
            byte[] buf = Files.readAllBytes(imageFile.toPath());
			out.write(buf);
		}
	}
}