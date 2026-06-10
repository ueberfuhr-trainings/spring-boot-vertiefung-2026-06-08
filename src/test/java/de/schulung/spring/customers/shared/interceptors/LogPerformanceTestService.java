package de.schulung.spring.customers.shared.interceptors;

import org.slf4j.event.Level;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class LogPerformanceTestService {

  @LogPerformance
  void doSth() {
  }

  @LogPerformance(Level.DEBUG)
  void doSthDebug() {
  }

}
