package com.quotespilot.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class QuoteConfigurations {

    @Value("${quote.api.url}")
    private String apiUrl;

    private static final Logger logger = LoggerFactory.getLogger(QuoteConfigurations.class);
    
    @Bean
    WebClient quoteClient(){
        logger.info("*******************apiUrl="+apiUrl);
        return WebClient.create(apiUrl);
    }
    
}
