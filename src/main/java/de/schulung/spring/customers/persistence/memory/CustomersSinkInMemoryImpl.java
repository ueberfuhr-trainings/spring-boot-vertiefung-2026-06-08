package de.schulung.spring.customers.persistence.memory;

import de.schulung.spring.customers.domain.Customer;
import de.schulung.spring.customers.domain.CustomerFetchOptions;
import de.schulung.spring.customers.domain.CustomersSink;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class CustomersSinkInMemoryImpl
  implements CustomersSink {

  private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

  @Override
  public Stream<Customer> findAll(CustomerFetchOptions options) {
    return customers
      .values()
      .stream();
  }

  @Override
  public Optional<Customer> findById(UUID uuid, CustomerFetchOptions options) {
    return Optional
      .ofNullable(customers.get(uuid));
  }

  @Override
  public void save(Customer customer) {
    if (customer.getUuid() == null) {
      customer.setUuid(UUID.randomUUID());
    }
    customers.put(customer.getUuid(), customer);
  }

  @Override
  public boolean existsById(UUID uuid) {
    return customers.containsKey(uuid);
  }

  @Override
  public void deleteById(UUID uuid) {
    customers.remove(uuid);
  }
}
