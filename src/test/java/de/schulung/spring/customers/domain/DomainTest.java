package de.schulung.spring.customers.domain;

import de.schulung.spring.customers.TestContextDefinition;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Domain Tests are still blackbox tests, but with mocked sink.
 */
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DomainTest.DomainTestConfiguration.class)
@TestContextDefinition
@MockitoBean(types = CustomersSink.class)
@RecordApplicationEvents
// Standard-Java-Annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DomainTest {

  @Configuration
  @ComponentScan(basePackageClasses = DomainTest.class)
  @ImportAutoConfiguration(classes = {
    ValidationAutoConfiguration.class
  })
  class DomainTestConfiguration {
  }

}
