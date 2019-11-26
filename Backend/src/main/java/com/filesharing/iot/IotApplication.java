package com.filesharing.iot;

import com.filesharing.iot.Chord.Chord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@SpringBootApplication
public class IotApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(IotApplication.class, args);
        if (args.length > 1) {
            if (args[0].toUpperCase().equals("CHORD")) {
                Chord.main(Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

@Configuration
class RestTemplateConfig {

    // Create a bean for restTemplate to call services
    @Bean     // Load balance between service instances running at different ports.
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

