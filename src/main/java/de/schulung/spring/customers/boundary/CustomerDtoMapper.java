package de.schulung.spring.customers.boundary;

import de.schulung.spring.customers.domain.Customer;
import de.schulung.spring.customers.domain.CustomerState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerDtoMapper {

  @Mapping(target = "address", ignore = true)
  Customer map(CustomerDto dto);

  default CustomerState mapState(String state) {
    return switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "locked" -> CustomerState.LOCKED;
      case "disabled" -> CustomerState.DISABLED;
      default -> throw new IllegalArgumentException("Invalid state: " + state);
    };
  }

  CustomerDto map(Customer customer);

  default String mapState(CustomerState state) {
    return switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

}
