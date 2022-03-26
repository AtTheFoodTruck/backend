package com.sesac;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class ModuleCommonApplicationTests {

    @Bean
    public AuditorAware<String> auditor() {

        // TODO security context에 저장된 user 정보를 return해줘야 함
        // 우선 랜덤 ID
        return () -> Optional.of(UUID.randomUUID().toString());
    }

    public void contextLoads() {

    }
}
