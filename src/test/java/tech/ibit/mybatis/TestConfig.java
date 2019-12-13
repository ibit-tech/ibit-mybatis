package tech.ibit.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.ibit.mybatis.plugin.ResultMapInterceptor;


@MapperScan("tech.ibit.mybatis.test.mapper")
@SpringBootApplication
@Configuration
public class TestConfig {

    @Bean
    public ResultMapInterceptor getResultMapInterceptor() {
        return new ResultMapInterceptor();
    }
}
