package de.schulung.spring.customers.domain;

import jakarta.validation.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
public class CustomersServiceTests {

  @Autowired
  CustomersService customersService;

  public static Stream<Arguments> provideInvalidCustomers() {
    return Stream.of(
      Arguments.of((Customer) null),
      Arguments.of(new Customer()),
      Arguments.of(
        new Customer()
          .setBirthdate(LocalDate.of(2005, Month.MAY, 5))
          .setState(CustomerState.ACTIVE)
          .setName(null)
      ),
      Arguments.of(
        new Customer()
          .setName("Tom Mayer")
          .setState(CustomerState.ACTIVE)
          .setBirthdate(LocalDate.now().minusYears(18).plusDays(1))
      )
    );
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCustomers")
  void shouldValidateCustomerOnCreate(Customer customer) {
    assertThatThrownBy(() -> customersService.createCustomer(customer))
      .isInstanceOf(ValidationException.class);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCustomers")
  void shouldValidateCustomerOnReplace(Customer customer) {
    assertThatThrownBy(() -> customersService.replaceCustomer(customer))
      .isInstanceOf(ValidationException.class);
  }

}
