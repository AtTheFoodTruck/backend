package com.sesac.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AppConfig {

    /**
     * Security에 맞는 JPA AuditorAware구현
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-30
    **/
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//    }
}
