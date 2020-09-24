package com.daibiao.signapp.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.interceptor.InterceptorConfig
 * @description 拦截器配置
 * @date 2020-03-18 17:29:00
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE","PUT")
                .allowedHeaders("Origin", "X-Requested-With", "content-Type"," Accept", "token")
                .maxAge(3600);
    }

    /**
     * 拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/static/*");
    }

    /**
     * 添加拦截器
     */
    @Bean
    public HandlerInterceptor getInterceptor(){
        return new AuthenticationInterceptor();
    }
}
