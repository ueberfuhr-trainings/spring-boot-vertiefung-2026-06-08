package de.schulung.spring.customers.boundary;

import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.IF_MATCH;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;
import static org.springframework.http.HttpHeaders.LINK;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpHeaders.ORIGIN;

@Configuration
/*
@ConditionalOnBooleanProperty(
  name = "application.cors.enabled",
  havingValue = true,
  matchIfMissing = false
)
*/
@ConditionalOnBooleanProperty("application.cors.enabled")
public class CorsConfiguration {

  @Bean
  WebMvcConfigurer corsConfigurer(
    CorsConfigurationProperties corsConfigurationProperties
  ) {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@Nonnull CorsRegistry registry) {
        registry
          .addMapping("/**")
          .exposedHeaders(LOCATION, LINK)
          .allowedHeaders(ORIGIN, CONTENT_TYPE, ACCEPT, ACCEPT_LANGUAGE, IF_MATCH, IF_NONE_MATCH, AUTHORIZATION)
          .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
          .allowedOriginPatterns(corsConfigurationProperties.getAllowedOrigins())
          .allowCredentials(corsConfigurationProperties.isAllowCredentials());
      }
    };
  }

}
