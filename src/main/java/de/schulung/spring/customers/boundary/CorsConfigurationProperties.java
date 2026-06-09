package de.schulung.spring.customers.boundary;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "application.cors")
public class CorsConfigurationProperties {

  private boolean enabled = false;
  private List<@NotBlank String> allowedOrigins = List.of("*");
  private boolean allowCredentials = false;

}
