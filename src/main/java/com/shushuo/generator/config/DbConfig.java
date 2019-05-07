package com.shushuo.generator.config;

import com.shushuo.generator.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据库配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class DbConfig {
    @Autowired
    private MySQLGeneratorDao mySQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao(){
        return mySQLGeneratorDao;
    }
}
