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
      
        login.addUrlPatterns("/back-end/*"); 
        return login;
    }
    @Bean
    public FilterRegistrationBean<PermissionFilter> permissionFilter() {
        FilterRegistrationBean<PermissionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PermissionFilter());
        registrationBean.addUrlPatterns("/back-end/member", "/back-end/memberorder","/storecarousel/listAllStorecarouseltest","/goodstype/listAllGoodsType");  // 設置過濾器適用的路徑
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<ManagerFilter> managerFilter() {
        FilterRegistrationBean<ManagerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ManagerFilter());
        registrationBean.addUrlPatterns("/goods/listAllCheckStatus", "/used/getAllSellerUsedListFragment","/goods/listAllCheckStatus","/manager/manager","/discount/listAllDiscount");  // 設置過濾器適用的路徑
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<CounterFilter> counterFilter() {
        FilterRegistrationBean<CounterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CounterFilter());
        registrationBean.addUrlPatterns("/back-end/counter", "/monthsettlement/addMonthSettlement","/coupon/approve");  // 設置過濾器適用的路徑
        return registrationBean;
    }
}
