package com.ff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class ReggietakeOutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggietakeOutApplication.class, args);
		log.info("正常启动了。。");
	}

}
