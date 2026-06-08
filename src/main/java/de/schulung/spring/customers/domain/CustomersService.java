package de.schulung.spring.customers.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Validated
@Service
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersSink sink;

  public Stream<Customer> getCustomers() {
    return sink
      .findAll();
  }

  public Stream<Customer> getCustomersByState(CustomerState state) {
    return sink
      .findByState(state);
  }

  public Optional<Customer> getCustomerByUuid(UUID uuid) {
    return sink
      .findById(uuid);
  }

  public void createCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
  }

  public boolean existsCustomerByUuid(UUID uuid) {
    return sink.existsById(uuid);
  }

  public void replaceCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
  }

  public void deleteCustomer(UUID uuid) {
    sink.deleteById(uuid);
  }


}
