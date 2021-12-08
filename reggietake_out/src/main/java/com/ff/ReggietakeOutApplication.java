package com.ff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
//开启组件扫描
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
public class ReggietakeOutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggietakeOutApplication.class, args);
		log.info("正常启动了。。");
	}

}
 