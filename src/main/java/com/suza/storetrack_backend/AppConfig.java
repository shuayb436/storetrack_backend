package com.suza.storetrack_backend;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
     // @Bean
    // public ModelMapper modelMapper() {
    // return new ModelMapper();
    // }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        // prevents overwriting existing DB fields with null.(wen updating entit and some fields r missing or null)
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    } 
}
