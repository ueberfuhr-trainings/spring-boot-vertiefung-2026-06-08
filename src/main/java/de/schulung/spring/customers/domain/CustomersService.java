package de.schulung.spring.customers.domain;

import de.schulung.spring.customers.shared.interceptors.LogPerformance;
import de.schulung.spring.customers.shared.interceptors.PublishEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;
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

  @LogPerformance
  @PublishEvent(CustomerCreatedEvent.class)
  @Validated({CustomerValidationGroups.Create.class, Default.class})
  public void createCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
  }

  public boolean existsCustomerByUuid(UUID uuid) {
    return sink.existsById(uuid);
  }

  @LogPerformance(Level.DEBUG)
  @PublishEvent(CustomerUpdatedEvent.class)
  @Validated({CustomerValidationGroups.Update.class, Default.class})
  public void replaceCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
  }

  @PublishEvent(CustomerDeletedEvent.class)
  public void deleteCustomer(UUID uuid) {
    sink.deleteById(uuid);
  }


}
