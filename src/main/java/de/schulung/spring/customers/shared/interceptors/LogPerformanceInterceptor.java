package de.schulung.spring.customers.shared.interceptors;

import de.schulung.spring.customers.shared.utilities.AnnotationUtility;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogPerformanceInterceptor
  implements MethodInterceptor {

  @Override
  public @Nullable Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
    final var config = AnnotationUtility
      .findAnnotation(
        invocation.getMethod(),
        LogPerformance.class
      );
    final var level = config
      .map(LogPerformance::value)
      .orElse(Level.INFO);
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
