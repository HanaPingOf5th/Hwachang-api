package com.hwachang.hwachangapi.utils.config;

import com.hwachang.hwachangapi.utils.randomGenerate.RandomCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {
    @Bean
    public RandomCodeGenerator randomCodeGenerator(){return new RandomCodeGenerator();}
}
