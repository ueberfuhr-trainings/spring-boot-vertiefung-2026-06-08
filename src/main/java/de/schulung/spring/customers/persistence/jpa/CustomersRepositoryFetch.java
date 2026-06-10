package de.schulung.spring.customers.persistence.jpa;

import de.schulung.spring.customers.domain.CustomerFetchOptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomersRepositoryFetch {

  private final EntityManager em;

  public List<CustomerEntity> findAll(CustomerFetchOptions options) {
    var query = em.createQuery(
      "SELECT c FROM Customer c", CustomerEntity.class);
    applyFetchOptions(query, options);
    return query.getResultList();
  }

  public Optional<CustomerEntity> findById(UUID uuid, CustomerFetchOptions options) {
    var query = em.createQuery(
      "SELECT c FROM Customer c WHERE c.uuid = :uuid", CustomerEntity.class);
    query.setParameter("uuid", uuid);
    applyFetchOptions(query, options);
    return query.getResultStream().findFirst();
  }

  public List<CustomerEntity> findByState(String state, CustomerFetchOptions options) {
    var query = em.createQuery(
      "SELECT c FROM Customer c WHERE c.state = :state", CustomerEntity.class);
    query.setParameter("state", state);
    applyFetchOptions(query, options);
    return query.getResultList();
  }

  private void applyFetchOptions(TypedQuery<?> query, CustomerFetchOptions options) {
    var entityGraph = em.createEntityGraph(CustomerEntity.class);
    if (options.isIncludeAddress()) {
      entityGraph.addSubgraph("address");
    }
    query.setHint(GraphSemantic.FETCH.getJakartaHintName(), entityGraph);
  }

}
