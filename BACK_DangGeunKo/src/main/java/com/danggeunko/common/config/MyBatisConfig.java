package com.danggeunko.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.danggeunko.*.dao")
public class MyBatisConfig {

}
