package de.schulung.spring.customers.domain;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CustomersSink {

  Stream<Customer> findAll(CustomerFetchOptions options);

  default Stream<Customer> findByState(CustomerState state, CustomerFetchOptions options) {
    return findAll(options)
      .filter(c -> c.getState().equals(state));
  }

  default Optional<Customer> findById(UUID uuid, CustomerFetchOptions options) {
    return findAll(options)
      .filter(c -> c.getUuid().equals(uuid))
      .findFirst();
  }

  void save(Customer customer);

  default boolean existsById(UUID uuid) {
    return findById(uuid, new CustomerFetchOptions().setIncludeAddress(false))
      .isPresent();
  }

  void deleteById(UUID uuid);

}
