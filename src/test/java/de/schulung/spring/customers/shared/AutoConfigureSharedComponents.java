package de.schulung.spring.customers.shared;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation at your test slices to get
 * shared components configured. It is not intended
 * to be used to directly annotate a test class.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackageClasses = AutoConfigureSharedComponents.class)
public @interface AutoConfigureSharedComponents {
}
