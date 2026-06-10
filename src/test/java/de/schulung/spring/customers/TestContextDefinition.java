package de.schulung.spring.customers;

import de.schulung.spring.customers.shared.AutoConfigureSharedComponents;
import de.schulung.spring.customers.shared.interceptors.AutoConfigureInterceptorTestComponents;
import de.schulung.spring.customers.testing.TableChangesConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation to define a custom test context for testing applications.
 * This annotation is specifically created to facilitate the setup of tests
 * by providing pre-configured aspects such as active profiles and other
 * relevant test configurations.
 * <p>
 * Features:
 * - Marks the annotated annotation type as a test-specific context definition.
 * - Applies the `test` profile to tests where this meta-annotation is used.
 * - Ensures that the test configurations are isolated under the specified
 * annotation definition, making tests more modular and reusable.
 * <p>
 * Use this annotation to create custom test configurations that encapsulate
 * the required test environment settings for specific integration or
 * end-to-end test cases.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@AutoConfigureSharedComponents
@AutoConfigureInterceptorTestComponents
@Import(TableChangesConfiguration.class)
public @interface TestContextDefinition {
}