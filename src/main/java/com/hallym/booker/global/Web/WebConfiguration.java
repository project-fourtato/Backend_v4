package com.hallym.booker.global.Web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:3000")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PUT.name()
                )
                .allowCredentials(true) // 쿠키나 인증 헤더 정보를 포함시켜 요청하고 싶을 때 필요
                .allowedHeaders("*"); //클라이언트 CORS 허용
    }
}
