package de.schulung.spring.customers.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFetchOptions {

  public static CustomerFetchOptions DEFAULT = new CustomerFetchOptions();

  private boolean includeAddress = true;

}
