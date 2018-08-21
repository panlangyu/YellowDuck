package com.duck.yellowduck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.duck.yellowduck.domain.dao"})
@ComponentScan({"com.duck.yellowduck.*"})
public class YellowduckApplication {

    public static void main(String[] args) {
        SpringApplication.run(YellowduckApplication.class, args);
    }
}
