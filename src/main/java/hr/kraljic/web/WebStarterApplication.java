package hr.kraljic.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("hr.kraljic.web")
public class WebStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStarterApplication.class, args);
    }

}
