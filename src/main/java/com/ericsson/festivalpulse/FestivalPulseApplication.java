package com.ericsson.festivalpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class FestivalPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FestivalPulseApplication.class, args);
    }

}
