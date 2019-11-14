package com.filesharing.iot;

import com.filesharing.iot.Chord.Chord;
import com.filesharing.iot.Chord.Query;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;


@SpringBootApplication
public class IotApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotApplication.class, args);
        if(args.length > 1){
            if(args[0].toUpperCase().equals("CHORD")){
                Chord.main(Arrays.copyOfRange(args, 1, args.length));
            }
            else{
                if(args[0].toUpperCase().equals("QUERY")){
                    Query.main((Arrays.copyOfRange(args, 1, args.length)));
                }
            }
        }


    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
