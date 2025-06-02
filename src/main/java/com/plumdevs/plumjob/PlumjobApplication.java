package com.plumdevs.plumjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/* This is the standard application driver code, no need to modify anything here
Compile and run this to run the app
*/

@SpringBootApplication()
public class PlumjobApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlumjobApplication.class, args);
	}

}
