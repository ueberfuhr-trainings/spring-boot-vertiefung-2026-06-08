package de.schulung.spring.customers.infrastructure;

import de.schulung.spring.customers.domain.CustomerCreatedEvent;
import de.schulung.spring.customers.domain.CustomerDeletedEvent;
import de.schulung.spring.customers.domain.CustomerUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventsLogger {

  @EventListener
  public void onCustomerCreated(CustomerCreatedEvent event) {
    log.info("Customer created: {}", event.customer().getUuid());
  }

  @EventListener
  public void onCustomerUpdated(CustomerUpdatedEvent event) {
    log.info("Customer updated: {}", event.customer().getUuid());
  }

  @EventListener
  public void onCustomerDeleted(CustomerDeletedEvent event) {
    log.info("Customer deleted: {}", event.uuid());
  }

}
