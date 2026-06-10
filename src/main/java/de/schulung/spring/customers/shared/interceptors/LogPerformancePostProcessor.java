package de.schulung.spring.customers.shared.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
      // where?
      new AnnotationMatchingPointcut(null, LogPerformance.class, true),
      // what?
      new MethodInterceptor() {
        @Override
        public @Nullable Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
          var ts = System.currentTimeMillis();
          try {
            // Weiterleitung an Originalobjekt
            return invocation.proceed();
          } finally {
            var ts2 = System.currentTimeMillis();
            log.info("{} took {} ms", invocation.getMethod().getName(), (ts2 - ts));
          }
        }
      }
    );
  }

}
