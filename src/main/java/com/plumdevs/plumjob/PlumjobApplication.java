package com.plumdevs.plumjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/* This is the standard application driver code, no need to modify anything here
Compile and run this to run the app
*/

@SpringBootApplication()
@EnableScheduling
public class PlumjobApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlumjobApplication.class, args);
	}

}
