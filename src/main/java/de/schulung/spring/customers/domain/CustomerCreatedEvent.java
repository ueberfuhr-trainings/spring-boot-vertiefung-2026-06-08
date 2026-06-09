package de.schulung.spring.customers.domain;

public record CustomerCreatedEvent(
  Customer customer
) {
}
