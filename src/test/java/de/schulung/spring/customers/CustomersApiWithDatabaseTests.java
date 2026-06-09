package de.schulung.spring.customers;

import com.jayway.jsonpath.JsonPath;
import de.schulung.spring.customers.testing.TableChanges;
import org.assertj.db.type.Changes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomersApplicationTest
public class CustomersApiWithDatabaseTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  @TableChanges("customers")
  private Changes changes;

  @Test
  void shouldCreateCustomerInDatabase() throws Exception {

    changes.setStartPointNow();

    // Create the customer
    final var responseBody = mockMvc
      .perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "Tom Mayer",
              "birthdate": "2005-05-15",
              "state": "active"
            }
            """)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    final var uuid = JsonPath.read(responseBody, "$.uuid");

    changes.setEndPointNow();

    assertThat(changes)
      .hasNumberOfChanges(1)
      .change().isCreation()
      .rowAtEndPoint()
      .value("uuid").isEqualTo(UUID.fromString(uuid.toString()))
      .value("name").isEqualTo("Tom Mayer")
      .value("birth_date").isEqualTo(LocalDate.of(2005, Month.MAY, 15))
      .value("state").isEqualTo("active");

  }

}
