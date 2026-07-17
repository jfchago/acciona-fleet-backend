package com.company.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
                .allowedMethods(RequestMethod.GET.name(), RequestMethod.POST.name(), RequestMethod.PATCH.name(), RequestMethod.PUT.name(), RequestMethod.DELETE.name(), RequestMethod.OPTIONS.name())
                .allowedHeaders("Accept", "Content-Type", "Authorization", "If-Match", "If-None-Match")
                .exposedHeaders("Content-Disposition", "ETag")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
