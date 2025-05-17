package com.core.halpme.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedMethod("*");        // GET, POST, PUT, DELETE, OPTIONS 등 모두 허용
        config.addAllowedHeader("*");        // 모든 요청 헤더 허용
        config.setAllowCredentials(true);    // 쿠키, 인증정보 포함 여부 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("*") // 어떤 origin이든 허용 (swagger-ui 때문에 필요)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용 메서드
                .allowedHeaders("*");
    }
}