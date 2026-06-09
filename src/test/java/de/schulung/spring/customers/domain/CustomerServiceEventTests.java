package de.schulung.spring.customers.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
public class CustomerServiceEventTests {

  @Autowired
  ApplicationEvents applicationEvents;
  @Autowired
  CustomersService customersService;

  @Test
  void shouldPublishEventOnCustomerCreated() {

    var customer = new Customer()
      .setName("John Doe")
      .setBirthdate(LocalDate.of(1980, Month.APRIL, 1));
    customersService.createCustomer(customer);

    assertThat(applicationEvents.stream(CustomerCreatedEvent.class))
      .hasSize(1)
      .first()
      .extracting(CustomerCreatedEvent::customer)
      .isSameAs(customer);

  }

  @Test
  void shouldNotPublishEventOnInvalidCustomerCreated() {

    var customer = new Customer()
      .setBirthdate(LocalDate.of(1980, Month.APRIL, 1));
    assertThatThrownBy(() -> customersService.createCustomer(customer))
      .isNotNull();
    assertThat(applicationEvents.stream(CustomerCreatedEvent.class))
      .isEmpty();

  }

  @Test
  void shouldPublishEventOnCustomerUpdated() {

    var customer = new Customer()
      .setName("John Doe")
      .setBirthdate(LocalDate.of(1980, Month.APRIL, 1));
    customersService.createCustomer(customer);
    customersService.replaceCustomer(customer);

    assertThat(applicationEvents.stream(CustomerUpdatedEvent.class))
      .hasSize(1)
      .first()
      .extracting(CustomerUpdatedEvent::customer)
      .isSameAs(customer);

  }

  @Test
  void shouldPublishEventOnCustomerDeleted() {

    var customer = new Customer()
      .setName("John Doe")
      .setBirthdate(LocalDate.of(1980, Month.APRIL, 1));
    customersService.createCustomer(customer);
    customersService.deleteCustomer(customer.getUuid());

    assertThat(applicationEvents.stream(CustomerDeletedEvent.class))
      .hasSize(1)
      .first()
      .extracting(CustomerDeletedEvent::uuid)
      .isEqualTo(customer.getUuid());

  }

}
