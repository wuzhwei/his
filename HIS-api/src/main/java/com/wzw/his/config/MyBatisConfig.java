package com.wzw.his.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.wzw.his.mbg"})//"com.wzw.his.sms.dao",
public class MyBatisConfig {
}
