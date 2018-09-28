package cn.wolfcode.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *@Component 主键标签
 * web配置
 */
@Component
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter{
    @Autowired
    private LoginInterceptor loginInterceptor;

    //添加拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
