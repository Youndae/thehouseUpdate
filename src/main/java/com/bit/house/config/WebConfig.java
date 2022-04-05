package com.bit.house.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.bit.house")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");

        registry.addResourceHandler("/icon/**")
                .addResourceLocations("classpath:/static/icon/");

        registry.addResourceHandler("/uploadImg/**")
                .addResourceLocations("/image/");

        registry.addResourceHandler("/editor/**")
                .addResourceLocations("classpath:/editor/");

        registry.addResourceHandler("/plugins/**")
                .addResourceLocations("classpath:/static/plugins/");

//        registry.addResourceHandler("/seller/plugins/**")
//                .addResourceLocations("classpath:/static/plugins/");
//
//        registry.addResourceHandler("/seller/js/**")
//                .addResourceLocations("classpath:/static/js/");
//
//        registry.addResourceHandler("/seller/css/**")
//                .addResourceLocations("classpath:/static/css/");
//
//        registry.addResourceHandler("/seller/icon/**")
//                .addResourceLocations("classpath:/static/icon/");

    }
}
