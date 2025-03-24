package com.studies.springcoredemo.config;

import com.studies.springcoredemo.common.Coach;
import com.studies.springcoredemo.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {

    @Bean("customId")
    public Coach swimCoach() {
        return new SwimCoach();
    }
}
