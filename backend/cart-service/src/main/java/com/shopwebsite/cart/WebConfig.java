package com.shopwebsite.cart; import org.springframework.context.annotation.Configuration;import org.springframework.web.servlet.config.annotation.*;
@Configuration public class WebConfig implements WebMvcConfigurer { public void addCorsMappings(CorsRegistry r){r.addMapping("/**").allowedOrigins("http://localhost:5173").allowedMethods("*").allowedHeaders("*");} }
