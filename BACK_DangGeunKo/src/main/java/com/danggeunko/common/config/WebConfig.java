package com.danggeunko.common.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.danggeunko.common.interceptor.AdminInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	AdminInterceptor adminInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor).addPathPatterns("/users");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST");
	}
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ⭐ 실제 이미지 폴더 위치 계산 (백엔드 폴더의 부모 폴더 아래 profileImg)
        String rootPath = new File(System.getProperty("user.dir")).getParent();
        String profileImgPath = "file:///" + rootPath + File.separator + "profileImg" + File.separator;

        // http://localhost:8080/profileImg/파일명 으로 요청이 오면 응답
        registry.addResourceHandler("/profileImg/**")
                .addResourceLocations(profileImgPath);
    }
}
