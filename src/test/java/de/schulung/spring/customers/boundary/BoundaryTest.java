package de.schulung.spring.customers.boundary;

import de.schulung.spring.customers.TestContextDefinition;
import de.schulung.spring.customers.domain.CustomersService;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Composite annotation for configuring and testing the boundary layer
 * of a Spring MVC application. This annotation integrates various
 * configurations and behaviors to streamline boundary testing setups.
 * <p>
 * Annotations included:
 * - {@code @WebMvcTest}: Configures Spring MVC for testing controllers,
 * focusing on the boundary layer without starting the entire application context.
 * - {@code @ComponentScan}: Restricts scanning to the base package of
 * the annotated test class to detect components needed for the boundary tests.
 * - {@code @MockitoBean}: Mocks specified types for dependency injection; in
 * this case, {@code CustomersService} is mocked.
 * - {@code @AutoConfigureCors}: Configures CORS settings and allows limiting
 * allowed origins for requests during testing.
 * - {@code @ActiveProfiles}: Sets the active Spring profile for the
 * test environment to "test".
 * <p>
 * Usage:
 * Apply this annotation on your test classes that are meant to verify
 * the behavior of boundary controllers or similar components. This helps
 * simulate real-world scenarios like CORS restrictions, MVC behavior,
 * and mock service dependencies without running the entire application.
 */
// https://docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html
@WebMvcTest
@ComponentScan(basePackageClasses = BoundaryTest.class)
@TestContextDefinition
@AutoConfigureMockMvc
@AutoConfigureCors(allowedOrigins = {"*.swagger.io", "localhost:8080"})
@MockitoBean(types = CustomersService.class)
// Standard-Java-Annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BoundaryTest {

}
