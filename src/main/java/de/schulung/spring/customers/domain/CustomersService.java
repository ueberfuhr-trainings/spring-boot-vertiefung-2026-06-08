package de.schulung.spring.customers.domain;

import de.schulung.spring.customers.shared.interceptors.LogPerformance;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
  private final ApplicationEventPublisher eventPublisher;

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
  @Validated({CustomerValidationGroups.Create.class, Default.class})
  public void createCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
    eventPublisher.publishEvent(new CustomerCreatedEvent(customer));
  }

  public boolean existsCustomerByUuid(UUID uuid) {
    return sink.existsById(uuid);
  }

  @LogPerformance
  @Validated({CustomerValidationGroups.Update.class, Default.class})
  public void replaceCustomer(@NotNull @Valid Customer customer) {
    sink.save(customer);
    eventPublisher.publishEvent(new CustomerUpdatedEvent(customer));
  }

  public void deleteCustomer(UUID uuid) {
    sink.deleteById(uuid);
    eventPublisher.publishEvent(new CustomerDeletedEvent(uuid));
  }


}
