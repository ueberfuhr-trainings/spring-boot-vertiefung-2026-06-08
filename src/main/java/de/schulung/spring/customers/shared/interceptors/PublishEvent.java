package de.schulung.spring.customers.shared.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to publish an event when the annotated method is executed successfully.
 * This annotation can be applied to methods to indicate that an event should be dispatched
 * after the method completes. The event class specified in the value of this annotation
 * must have a constructor that matches the parameters of the annotated method.
 * <p>
 * Key Features:
 * - Associates an event class with the method, which will be instantiated with the
 * method's parameters during execution.
 * - Leveraged by interceptors, like {@code PublishEventInterceptor}, to handle event
 * instantiation and publication automatically.
 * - Supports use cases where method calls trigger relevant application events,
 * allowing for event-driven processing.
 * <p>
 * Usage Requirements:
 * - The event class specified in the {@link #value()} must provide a constructor
 * that matches the signature of the method it annotates.
 * <p>
 * Example Scenarios:
 * - Implementing domain events in DDD (Domain-Driven Design).
 * - Triggering notifications or workflows based on specific method executions.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PublishEvent {

  /**
   * Specifies the event class that should be published when the annotated method completes successfully.
   * The event class must provide a constructor that matches the parameters of the annotated method.
   *
   * @return the class type of the event that will be published
   */
  Class<?> value();

}
