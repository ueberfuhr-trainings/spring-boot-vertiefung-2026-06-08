package de.schulung.spring.customers.domain;

import de.schulung.spring.customers.shared.validation.Adult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Customer {

  @NotNull(groups = CustomerValidationGroups.Update.class)
  @Null(groups = CustomerValidationGroups.Create.class)
  private UUID uuid;
  @NotNull
  private String name;
  @NotNull
  @Adult
  private LocalDate birthdate;
  @NotNull
  private CustomerState state = CustomerState.ACTIVE;


}
