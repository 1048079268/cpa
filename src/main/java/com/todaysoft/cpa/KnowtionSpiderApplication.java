package com.todaysoft.cpa;

import com.todaysoft.cpa.param.CPAProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({CPAProperties.class})
@EnableScheduling
@EnableAsync
public class KnowtionSpiderApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowtionSpiderApplication.class, args);
	}
}
