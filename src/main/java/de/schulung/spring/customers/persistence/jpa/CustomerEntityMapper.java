package de.schulung.spring.customers.persistence.jpa;

import de.schulung.spring.customers.domain.Customer;
import de.schulung.spring.customers.domain.CustomerState;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
  componentModel = "spring",
  uses = AddressEntityMapper.class
)
public interface CustomerEntityMapper {

  Customer map(CustomerEntity entity);

  default CustomerState mapState(String state) {
    return switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "locked" -> CustomerState.LOCKED;
      case "disabled" -> CustomerState.DISABLED;
      default -> throw new IllegalArgumentException("Invalid state: " + state);
    };
  }

  CustomerEntity map(Customer customer);

  default String mapState(CustomerState state) {
    return switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  void copy(CustomerEntity entity, @MappingTarget Customer customer);

}
