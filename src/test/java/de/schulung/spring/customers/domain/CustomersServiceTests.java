package de.schulung.spring.customers.domain;

import jakarta.validation.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class CustomersServiceTests {

  @Autowired
  CustomersService customersService;

  private static Customer createValidCustomer() {
    return new Customer()
      .setName("Tom Mayer")
      .setBirthdate(LocalDate.now().minusYears(18))
      .setState(CustomerState.ACTIVE);
  }

  private static Stream<Customer> provideInvalidCustomersForAllModifications(boolean withUuid) {
    return Stream.of(
        null,
        createValidCustomer()
          .setName(null),
        createValidCustomer()
          .setBirthdate(null),
        createValidCustomer()
          .setBirthdate(LocalDate.now().minusYears(18).plusDays(1))
      )
      .map(c -> c == null || !withUuid ? c : c.setUuid(UUID.randomUUID()));
  }

  public static Stream<Arguments> provideInvalidCustomersForCreation() {
    return Stream.concat(
      provideInvalidCustomersForAllModifications(false),
      Stream.of(
        createValidCustomer()
          .setUuid(UUID.randomUUID())
      )

    ).map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCustomersForCreation")
  void shouldValidateCustomerOnCreate(Customer customer) {
    assertThatThrownBy(() -> customersService.createCustomer(customer))
      .isInstanceOf(ValidationException.class);
  }

  public static Stream<Arguments> provideInvalidCustomersForReplacement() {
    return Stream.concat(
      provideInvalidCustomersForAllModifications(true),
      Stream.of(
        createValidCustomer()
          .setUuid(null)
      )

    ).map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCustomersForReplacement")
  void shouldValidateCustomerOnReplace(Customer customer) {
    assertThatThrownBy(() -> customersService.replaceCustomer(customer))
      .isInstanceOf(ValidationException.class);
  }

}
