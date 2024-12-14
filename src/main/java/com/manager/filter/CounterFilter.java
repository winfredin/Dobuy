package com.manager.filter;

import org.springframework.stereotype.Component;

import com.managerauth.model.ManagerAuthVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CounterFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 獲取 session 中的登錄信息
        List<ManagerAuthVO> managerAuthList = (List<ManagerAuthVO>) httpRequest.getSession().getAttribute("auth");
        Integer loggedInManagerNo = (Integer) httpRequest.getSession().getAttribute("managerNo");

        boolean isAdmin = false;
        if (managerAuthList != null && loggedInManagerNo != null) {
            for (ManagerAuthVO managerAuth : managerAuthList) {
                if (managerAuth.getManagerNo().getManagerNo().equals(loggedInManagerNo)) {
                    if ("超級管理員".equals(managerAuth.getAuthNo().getAuthTitle()) || "櫃位管理員".equals(managerAuth.getAuthNo().getAuthTitle())) {
                        isAdmin = true;
                        break;
                    }
                }
            }
        }

        // 如果不是超級管理員，則拒絕訪問
        if (!isAdmin) {
            httpResponse.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = httpResponse.getWriter();
            writer.println("<script type='text/javascript'>");
            writer.println("alert('不是超管或櫃位管理員，無法訪問此頁面！');");
            writer.println("window.location.href = '" + httpRequest.getContextPath() + "/back-end-homepage';");
            writer.println("</script>");
            return;
        }

        // 否則繼續處理請求
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化過濾器配置
    }

    @Override
    public void destroy() {
        // 釋放資源
    }
}
