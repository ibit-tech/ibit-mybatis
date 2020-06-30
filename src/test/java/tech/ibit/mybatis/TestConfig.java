package tech.ibit.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@MapperScan(basePackages = {"tech.ibit.mybatis.demo.mapper", "tech.ibit.mybatis.demo.ext.mapper"})
@SpringBootApplication
@Configuration
public class TestConfig {

//    @Bean
//    public ResultMapInterceptor getResultMapInterceptor() {
//        return new ResultMapInterceptor();
//    }
}
