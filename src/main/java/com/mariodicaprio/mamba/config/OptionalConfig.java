package com.mariodicaprio.mamba.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


/**
 * This class contains optional configurations for the application.
 */
@Configuration
@SuppressWarnings("unused")
public class OptionalConfig {

    /**
     * Enables the logging of handled http requests.
     * @return The {@code @Bean} that enables the above-mentioned functionality
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }

}
