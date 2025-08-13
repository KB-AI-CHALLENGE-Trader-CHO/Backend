package com.kbai.tradejoe;

import com.kbai.tradejoe.config.ScoringProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(ScoringProperties.class)
public class TradejoeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradejoeApplication.class, args);
    }

}
