package com.qcw.parksys.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    MyIntercepter myIntercepter;

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
                .excludePathPatterns("/parksys/space/spacelist");

    }
}


