package com.demosix.demosix;

import com.demosix.demosix.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
//@MapperScan("com.demosix.demosix")
//springboot 中使用拦截器需要继承WebMvcConfigurationSupport类
public class DemosixApplication extends WebMvcConfigurationSupport {

    @Autowired
    private AuthInterceptor authInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(DemosixApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        super.addInterceptors(registry);
    }

}
