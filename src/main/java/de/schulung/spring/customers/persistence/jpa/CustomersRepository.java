package de.schulung.spring.customers.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomersRepository
  extends JpaRepository<CustomerEntity, UUID> {

  List<CustomerEntity> findCustomerByState(String state);

}
