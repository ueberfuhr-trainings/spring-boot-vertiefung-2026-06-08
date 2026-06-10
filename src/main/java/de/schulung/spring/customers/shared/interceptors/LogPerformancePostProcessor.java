package de.schulung.spring.customers.shared.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;
import org.slf4j.event.Level;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogPerformancePostProcessor
  extends AbstractBeanFactoryAwareAdvisingPostProcessor
  implements InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    this.advisor = new DefaultPointcutAdvisor(
      // where? / when?
      new AnnotationMatchingPointcut(null, LogPerformance.class, true),
      // what?
      new MethodInterceptor() {
        @Override
        public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
          final var config = invocation.getMethod().getAnnotation(LogPerformance.class);
          final var level = config != null ? config.value() : Level.INFO;
          final var ts1 = System.currentTimeMillis();
          try {
            return invocation.proceed();
          } finally {
            final var ts2 = System.currentTimeMillis();
            log
              .atLevel(level)
              .log("Method {} took {} ms", invocation.getMethod().getName(), ts2 - ts1);
          }
        }
      }
    );
  }

}
