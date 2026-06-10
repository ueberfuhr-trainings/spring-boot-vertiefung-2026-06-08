package de.schulung.spring.customers.persistence.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomersRepository
  extends JpaRepository<CustomerEntity, UUID> {

  @EntityGraph(attributePaths = "address")
  List<CustomerEntity> findAllWithAddress();

  @EntityGraph(attributePaths = "address")
  Optional<CustomerEntity> findByIdWithAddress(UUID uuid);

  List<CustomerEntity> findCustomerByState(String state);

  @EntityGraph(attributePaths = "address")
  List<CustomerEntity> findCustomerByStateWithAddress(String state);

}
