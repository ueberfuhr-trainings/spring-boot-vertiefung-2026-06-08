package de.schulung.spring.customers;

import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation to configure and customize test behavior for the
 * Customer API provider application. This annotation integrates
 * multiple configurations necessary for testing within a Spring
 * Boot application context.
 * <p>
 * Features:
 * - Configures the testing environment with Spring Boot's testing support.
 * - Enables the use of {@code MockMvc} for testing web-related
 * functionality.
 * - Configures the test database by automatically setting up an
 * in-memory database.
 * - Wraps test methods in a transactional context. Changes made
 * during a test are rolled back after execution.
 * - Activates the `test` application profile for the testing environment.
 * <p>
 * Usage:
 * Designed for setting up integration or end-to-end tests of the
 * Customer API's behavior while interacting with the underlying
 * systems. Apply this annotation to your test classes to configure
 * the testing infrastructure with the outlined features.
 * <p>
 * Initialization of Sample Data:
 * The `initializationEnabled` attribute can be used to toggle the
 * initialization of sample customer data for the tests. This is
 * controlled via the `application.initialization.enabled` property.
 * By default, this initialization is disabled.
 */
@SpringBootTest
@TestContextDefinition
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RecordApplicationEvents
// Standard-Java-Annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CustomersApplicationTest {
}
