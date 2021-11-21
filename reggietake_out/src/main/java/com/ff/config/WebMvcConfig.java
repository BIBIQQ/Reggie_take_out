package com.ff.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author FF
 * @date 2021/11/17
 * @TIME:23:26
 */
@Slf4j
@Configuration
public class WebMvcConfig  extends WebMvcConfigurationSupport {
    /**
     * 开启静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态映射。。");

        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
    }
}
