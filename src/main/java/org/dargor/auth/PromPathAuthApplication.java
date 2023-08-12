package org.dargor.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class PromPathAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromPathAuthApplication.class, args);
    }

}
