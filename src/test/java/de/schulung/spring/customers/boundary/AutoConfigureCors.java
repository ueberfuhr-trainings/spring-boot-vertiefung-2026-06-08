package de.schulung.spring.customers.boundary;

import org.springframework.boot.test.context.PropertyMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation at your test slices to get
 * CORS properties configured. It is not intended
 * to be used to directly annotate a test class.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoConfigureCors {

  @PropertyMapping("application.cors.enabled")
  boolean enabled() default true;

  @PropertyMapping("application.cors.allowed-origins")
  String[] allowedOrigins() default "*";

  @PropertyMapping("application.cors.allow-credentials")
  boolean allowCredentials() default false;

}