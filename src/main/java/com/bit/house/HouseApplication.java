package com.bit.house;

import com.bit.house.security.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class HouseApplication implements WebMvcConfigurer {


    @Autowired
    private UserArgumentResolver userArgumentResolver;


    public static void main(String[] args) {
        SpringApplication.run(HouseApplication.class, args);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add((HandlerMethodArgumentResolver) userArgumentResolver);
    }

}