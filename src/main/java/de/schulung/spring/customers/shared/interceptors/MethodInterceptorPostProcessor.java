package de.schulung.spring.customers.shared.interceptors;

import lombok.RequiredArgsConstructor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;

/**
 * A post-processor implementation that configures method-level or class-level interception
 * using a specified {@link Pointcut} and {@link Advice}. This class integrates with the Spring
 * AOP framework, enabling the dynamic application of advice to beans that match the given
 * pointcut expression.
 *
 * <h3>Key Features:</h3>
 * - Associates a given pointcut with a specified advice through a {@link DefaultPointcutAdvisor}.
 * - Acts as a factory-aware advising post-processor, allowing seamless integration with
 * the Spring ApplicationContext lifecycle.
 * - Implements initialization logic post bean property population via {@link InitializingBean}.
 *
 * <h3>How It Works:</h3>
 * - During initialization, a {@link DefaultPointcutAdvisor} is created, binding the
 * defined pointcut and advice.
 * - The created advisor identifies target methods or classes that fulfill the criteria
 * of the pointcut, applying the specified advice dynamically at runtime.
 * <p>
 * This processor can be configured explicitly using {@link org.springframework.aop.support.annotation.AnnotationMatchingPointcut}
 * or custom pointcuts to target specific annotations, such as {@link LogPerformance}.
 * <p>
 * Dependencies:
 * - A {@link Pointcut} instance defining the target scope for interception.
 * - An {@link Advice} instance encapsulating the behavior to be applied.
 * <p>
 */
@RequiredArgsConstructor
public class MethodInterceptorPostProcessor
  extends AbstractBeanFactoryAwareAdvisingPostProcessor
  implements InitializingBean {

  private final Pointcut pointcut;
  private final Advice advice;

  @Override
  public void afterPropertiesSet() {
    this.advisor = new DefaultPointcutAdvisor(
      pointcut, // where?
      advice // what? (interceptor)
    );
  }

}