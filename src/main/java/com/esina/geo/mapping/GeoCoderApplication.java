package com.esina.geo.mapping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class GeoCoderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoCoderApplication.class, args);
    }
}