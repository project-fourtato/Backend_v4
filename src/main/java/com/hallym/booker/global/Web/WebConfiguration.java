package com.hallym.booker.global.Web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings (CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 안에 해당 주소를 넣어도 됨
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
                .exposedHeaders("Authorization", "RefreshToken")
                .allowCredentials(true);
    }
}
