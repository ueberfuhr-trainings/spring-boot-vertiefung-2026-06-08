package de.schulung.spring.customers.domain;

import java.util.UUID;

public record CustomerDeletedEvent(
  UUID uuid
) {
}
