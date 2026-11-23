package com.smurfchad.learning_monitoring_system;

import org.springframework.boot.SpringApplication;

public class TestLearningMonitoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(LearningMonitoringSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
