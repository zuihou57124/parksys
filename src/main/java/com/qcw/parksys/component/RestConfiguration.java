package com.qcw.parksys.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * 配置http
 */
@Configuration
public class RestConfiguration {

    @Autowired
    RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate build = builder.build();
        //配置 user-agent
        build.setInterceptors((Collections.singletonList(new UserAgentInterceptor())));
        //添加响应类型 text-html
        build.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());

        return build;
    }
}
