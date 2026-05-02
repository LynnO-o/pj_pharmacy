package com.example.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login", "/register", "/files/**")
                .excludePathPatterns("/slideshow/selectAll", "/drug/selectAll", "/goods/selectAll", "/drugShop/selectRank",
                "/address/selectAll", "/drug/selectById/**", "/drugShop/selectById/**",
                "/drug/selectPage", "/goods/selectPage", "/drugShop/selectPage", "/drugType/selectAll", "/goodsType/selectAll",
                "/forum/selectPage", "/comment/selectTree/**", "/forum/updateViewCount/**", "/comment/selectCount/**",
                "/forum/selectById/**")
        ;
    }

}
