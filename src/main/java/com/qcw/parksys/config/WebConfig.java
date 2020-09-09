package com.qcw.parksys.config;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    MyIntercepter myIntercepter;


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        // 对响应头进行CORS授权
        MyCorsRegistration corsRegistration = new MyCorsRegistration("/**");
        corsRegistration.allowedOrigins("*")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type",
                        "Last-Modified", "device", "token")
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                .allowCredentials(true)
                .maxAge(3600);

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsRegistration.getCorsConfiguration());
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        return new FilterRegistrationBean(corsFilter);

    }
    //配置跨域
    //只能接收本机的跨域请求
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                //允许访问的主机
//                .allowedOrigins("*")
//                //允许访问的方法
//                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
//                //允许接收cookie
//                .allowCredentials(true)
//                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type",
//                        "Last-Modified", "device", "token")
//                .maxAge(3600);
//
//    }

    /**
     * @param registry
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/parksys/user/login")
                .excludePathPatterns("/parksys/user/getcode")
                .excludePathPatterns("/parksys/user/register")
                .excludePathPatterns("/parksys/space/spacelist")
                .excludePathPatterns("/parksys/oss/**")
                .excludePathPatterns("/parksys/user/sendCodeToEmail")
                .excludePathPatterns("/parksys/order/test/**");

    }
}


