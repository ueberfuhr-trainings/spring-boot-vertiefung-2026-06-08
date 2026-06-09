package de.schulung.spring.customers.boundary;

import de.schulung.spring.customers.domain.CustomersService;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html
@WebMvcTest
@ComponentScan(basePackageClasses = BoundaryTest.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockitoBean(types = CustomersService.class)
// Standard-Java-Annotations
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BoundaryTest {
}
