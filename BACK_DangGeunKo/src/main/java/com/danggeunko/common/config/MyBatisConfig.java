package com.danggeunko.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ssafy.mvc.model.dao")
public class MyBatisConfig {

}
