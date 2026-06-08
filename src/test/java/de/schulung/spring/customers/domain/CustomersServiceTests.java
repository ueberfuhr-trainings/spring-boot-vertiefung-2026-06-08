package de.schulung.spring.customers.domain;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class CustomersServiceTests {

  @Autowired
  CustomersService customersService;

  @Test
  void shouldValidateOnCreateCustomerWithNull() {
    assertThatThrownBy(() -> customersService.createCustomer(null))
      .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldValidateOnCreateCustomerWithInvalidCustomer() {
    assertThatThrownBy(() -> customersService.createCustomer(new Customer()))
      .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldValidateOnReplaceCustomerWithNull() {
    assertThatThrownBy(() -> customersService.replaceCustomer(null))
      .isInstanceOf(ValidationException.class);
  }

  @Test
  void shouldValidateOnReplaceCustomerWithInvalidCustomer() {
    var customer = new Customer();
    customer.setName("Tom Mayer");
    customer.setBirthdate(LocalDate.of(2005, Month.MAY, 5));
    customersService.createCustomer(customer);

    customer.setName(null);

    assertThatThrownBy(() -> customersService.replaceCustomer(customer))
      .isInstanceOf(ValidationException.class);
  }

}
