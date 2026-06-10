package de.schulung.spring.customers.persistence.jpa;

import de.schulung.spring.customers.domain.Customer;
import de.schulung.spring.customers.domain.CustomerFetchOptions;
import de.schulung.spring.customers.domain.CustomerState;
import de.schulung.spring.customers.domain.CustomersSink;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(
  name = "application.persistence.type",
  havingValue = "jpa",
  matchIfMissing = true
)
@RequiredArgsConstructor
public class CustomersSinkJpaImpl
  implements CustomersSink {

  private final CustomersRepository repo;
  private final CustomersRepositoryFetch fetch;
  private final CustomerEntityMapper mapper;


  @Override
  public Stream<Customer> findAll(CustomerFetchOptions options) {
    return fetch
      .findAll(options)
      .stream()
      .map(mapper::map);
  }

  @Override
  public Stream<Customer> findByState(CustomerState state, CustomerFetchOptions options) {
    return fetch
      .findByState(mapper.mapState(state), options)
      .stream()
      .map(mapper::map);
  }

  @Override
  public Optional<Customer> findById(UUID uuid, CustomerFetchOptions options) {
    return fetch
      .findById(uuid, options)
      .map(mapper::map);
  }

  @Override
  public void save(Customer customer) {
    var entity = mapper.map(customer);
    repo.save(entity);
    // customer.setUuid(entity.getUuid());
    mapper.copy(entity, customer);
  }

  @Override
  public boolean existsById(UUID uuid) {
    return repo.existsById(uuid);
  }

  @Override
  public void deleteById(UUID uuid) {
    repo.deleteById(uuid);
  }
}
