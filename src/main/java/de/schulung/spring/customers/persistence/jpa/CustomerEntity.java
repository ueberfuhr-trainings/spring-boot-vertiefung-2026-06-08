package de.schulung.spring.customers.persistence.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer") // Name im JPQL
@Table(name = "customers") // Datenbank-Tabellenname
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;
  @NotNull
  private String name;
  @NotNull
  @Column(name = "birth_date")
  private LocalDate birthdate;
  @Pattern(regexp = "active|locked|disabled")
  private String state = "active";

  @OneToOne(
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  @JoinColumn(name = "address_uuid")
  private AddressEntity address;

}
