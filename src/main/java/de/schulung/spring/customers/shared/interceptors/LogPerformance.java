package de.schulung.spring.customers.shared.interceptors;

import org.slf4j.event.Level;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log the runtime performance of a method.
 * When applied to a method, this annotation allows logging
 * the time taken for the method execution. The logging level
 * can be specified using the value property.
 * <p>
 * Logging is performed by intercepting method calls,
 * and the logged information typically includes the method name
 * and the elapsed time in milliseconds.
 * <p>
 * The default logging level is INFO unless specified otherwise.
 * <p>
 * This annotation is supported by the {@code LogPermancePostProcessor},
 * which utilizes a method interceptor to calculate and log execution time.
 * <p>
 * Attributes:
 * - {@code value}: The logging level for performance logs. Defaults to {@code Level.INFO}.
 * <p>
 * Usage:
 * Apply this annotation to public methods to measure and log their performance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface LogPerformance {

  /**
   * Specifies the logging level used to log the runtime performance of the annotated method.
   * This level determines the severity of the log entry when recording the execution time.
   * The default logging level is INFO.
   *
   * @return the logging level for performance logs
   */
  Level value() default Level.INFO;

}