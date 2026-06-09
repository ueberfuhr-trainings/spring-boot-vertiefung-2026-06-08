package de.schulung.spring.customers.testing;

import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Changes;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.util.Arrays;

@TestConfiguration
public class TableChangesConfiguration {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public Changes changes(DataSource dataSource, InjectionPoint injectionPoint) {

    final var annotation = injectionPoint.getAnnotatedElement()
      .getAnnotation(TableChanges.class);

    if (annotation == null) {
      throw new IllegalStateException(
        "@TableChanges is required for injecting Changes but was missing at injection point: "
          + injectionPoint.getMember()
      );
    }

    final var builder = AssertDbConnectionFactory
      .of(dataSource)
      .create()
      .changes();

    Arrays
      .stream(annotation.value())
      .forEach(builder::table);

    return builder.build();

  }

}
