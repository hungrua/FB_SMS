package com.signupfacebook.Newlife_project_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.signupfacebook.Newlife_project_1")
@EntityScan("com.signupfacebook.Newlife_project_1.model")
public class NewlifeProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(NewlifeProject1Application.class, args);
	}

}
