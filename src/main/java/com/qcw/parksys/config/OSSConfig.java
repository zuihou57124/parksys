package com.qcw.parksys.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class OSSConfig {

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    public String endpoint;

    @Value("${spring.cloud.alicloud.access-key}")
    public String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    public String secretId;

    @Bean(value = "oss")
    @Scope("prototype")
    public OSS ossClient() {

        return new OSSClientBuilder().build(endpoint, accessId, secretId);

    }

}
