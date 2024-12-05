package com.manager.filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoginFilter> loggingFilter() {
        FilterRegistrationBean<LoginFilter> login = new FilterRegistrationBean<>();
        login.setFilter(new LoginFilter());  // 註冊自定義的 Filter
      
        login.addUrlPatterns("/back-end-homepage"); 
        return login;
    }
}
