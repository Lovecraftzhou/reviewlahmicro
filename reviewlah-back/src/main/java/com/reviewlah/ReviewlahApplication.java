package com.reviewlah;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.reviewlah.db.dao")
@EnableKnife4j
public class ReviewlahApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewlahApplication.class, args);
    }

}
