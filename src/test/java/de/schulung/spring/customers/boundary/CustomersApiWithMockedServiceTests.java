package de.schulung.spring.customers.boundary;

import de.schulung.spring.customers.domain.Customer;
import de.schulung.spring.customers.domain.CustomerState;
import de.schulung.spring.customers.domain.CustomersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@BoundaryTest
public class CustomersApiWithMockedServiceTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired // Mocked
  CustomersService customersService;

  @Test
  void shouldReturn200WhenGetCustomerByUuidExisting() throws Exception {

    var uuid = UUID.randomUUID();
    var customer = new Customer();
    customer.setUuid(uuid);
    customer.setName("Tom Mayer");
    customer.setBirthdate(LocalDate.of(2005, 5, 5));
    customer.setState(CustomerState.ACTIVE);

    when(customersService.getCustomerByUuid(uuid))
      .thenReturn(Optional.of(customer));

    mockMvc
      .perform(get("/customers/{uuid}", uuid))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.uuid").value(uuid.toString()))
      .andExpect(jsonPath("$.name").value("Tom Mayer"))
      .andExpect(jsonPath("$.birthdate").value("2005-05-05"))
      .andExpect(jsonPath("$.state").value("active"));


  }

  @Test
  void shouldReturn404WhenGetCustomerByUuidNotExisting() throws Exception {

    var uuid = UUID.randomUUID();

    when(customersService.getCustomerByUuid(uuid))
      .thenReturn(Optional.empty());

    mockMvc
      .perform(get("/customers/{uuid}", uuid))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldNotCreateCustomerWithInvalidAccept() throws Exception {
    mockMvc
      .perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "Tom Mayer",
              "birthdate": "2026-05-05",
              "state": "active"
            }
            """)
          .accept(MediaType.APPLICATION_XML)
      )
      .andExpect(status().isNotAcceptable());

    verify(customersService, never())
      .createCustomer(any());

  }

}
