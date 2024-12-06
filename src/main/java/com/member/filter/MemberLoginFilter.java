package com.member.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.model.MemberVO;

public class MemberLoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// 【取得 session】
		HttpSession session = req.getSession();
		// 【從 session 判斷此user是否登入過】
		MemberVO memberVO = new MemberVO();
		Object memAccount = session.getAttribute("memAccount");
		
		if (session == null || memAccount == null) {
			// 未登錄，記住當前請求的路徑和參數
			String originalRequest = req.getRequestURI(); // 包含 ContextPath
			String queryString = req.getQueryString(); // 获取 GET 参数
			
			if (queryString != null) {
				originalRequest += "?" + queryString;
			}

			session.setAttribute("originalRequest", originalRequest);
			System.out.println(queryString);
			System.out.println(originalRequest);
			// 重定向到登入頁面
			res.sendRedirect(req.getContextPath() + "/mem/login");

		} else {
			// 已登入，繼續處理請求
		
			 
			chain.doFilter(request, response);
		}

	}

}
