package com.example.blog.config;

import org.springframework.context.annotation.Bean;
import  com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfig {

    @Bean
    public  Cloudinary getCloudinary(){
        Map config = new HashMap<>();
        config.put("cloud_name", "dqem2qeip");
        config.put("api_key", "534957662267788");
        config.put("api_secret", "MyWvLugRmBScgcCRKxl2OgRaI0c");
        config.put("secure", true);

        return new 	Cloudinary(config);
    }
}
