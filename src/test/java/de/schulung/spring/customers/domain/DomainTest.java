package de.schulung.spring.customers.domain;

import de.schulung.spring.customers.TestContextDefinition;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Domain Tests are still blackbox tests, but with mocked sink.
 */
@SpringBootTest
@TestContextDefinition
@AutoConfigureTestDatabase // TODO: disable other layers
@MockitoBean(types = CustomersSink.class)
// Standard-Java-Annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DomainTest {
}
