package com.hivescm.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableFeignClients
@EnableDiscoveryClient
@EnableEurekaClient
@ComponentScan(basePackages = {"com.hivescm"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}
}
