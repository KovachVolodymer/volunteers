package org.example.volunteerback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "org.example.volunteerback")
public class VolunteerbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolunteerbackApplication.class, args);
	}
}
