package com.hwachang.hwachangapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "화창 API",
                description = "디지털 하나로 5기 화창의 API 명세서입니다.",
                version = "v1"
        )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi HwaChangAPI(){
        String [] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("화창 API")
                .pathsToMatch(paths).build();
    }
}
