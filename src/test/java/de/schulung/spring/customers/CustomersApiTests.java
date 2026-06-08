package de.schulung.spring.customers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class CustomersApiTests {

  @Autowired
  MockMvc mockMvc;

  /*
   * GET /customers -> 200, Content-Type: application/json, Liste von Customers
   */

  @Test
  void shouldGetCustomersReturn200() throws Exception {
    mockMvc
      .perform(
        get("/customers")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$").isArray());
  }

  /*
   * GET /customers mit Accept: application/xml -> 406 Not Acceptable
   */
  @Test
  void shouldGetCustomersAsXmlReturnNotAcceptable() throws Exception {
    mockMvc
      .perform(
        get("/customers")
          .accept(MediaType.APPLICATION_XML)
      )
      .andExpect(status().isNotAcceptable());
  }

  /*
   * POST /customers mit
   * {
   *   "name": "Tom Mayer",
   *   "birthdate": "2026-05-05",
   *   "state": "active"
   * }
   * -> 201 Created, application/json, JSON prüfen?, Location: /customers/{uuid} (existiert)
   */
  @Test
  void shouldCreateCustomerReturn201() throws Exception {
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value("Tom Mayer"))
      .andExpect(jsonPath("$.birthdate").value("2026-05-05"))
      .andExpect(jsonPath("$.state").value("active"))
      .andExpect(jsonPath("$.uuid").isNotEmpty())
      // assert that Location header is set and contains an absolute URL
      .andExpect(result -> {
        final var locationHeader = result
          .getResponse()
          .getHeader("Location");
        assertThat(locationHeader)
          .isNotNull();
        final var uri = URI.create(locationHeader);
        assertThat(uri.isAbsolute()).isTrue();
      });

  }

  @Test
  void shouldSetDefaultActiveStateOnCreateCustomer() throws Exception {
    mockMvc
      .perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "name": "Tom Mayer",
              "birthdate": "2026-05-05"
            }
            """)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.state").value("active"));

  }

  @Test
  void shouldAllowReadingSingleCustomerByUuidAfterCreation() throws Exception {
    // setup
    var responseBody = mockMvc
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();
    var uuid = new ObjectMapper()
      .readTree(responseBody)
      .path("uuid")
      .asString();

    // Test
    mockMvc
      .perform(get("/customers/{uuid}", uuid))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.uuid").value(uuid))
      .andExpect(jsonPath("$.name").value("Tom Mayer"))
      .andExpect(jsonPath("$.birthdate").value("2026-05-05"))
      .andExpect(jsonPath("$.state").value("active"));

  }

  @Test
  void shouldAllowReadingSingleCustomerByLocationHeaderAfterCreation() throws Exception {
    // setup
    var locationHeader = mockMvc
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getHeader("Location");

    assertThat(locationHeader)
      .isNotNull();

    // Test
    mockMvc
      .perform(get(locationHeader))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.uuid").isNotEmpty())
      .andExpect(jsonPath("$.name").value("Tom Mayer"))
      .andExpect(jsonPath("$.birthdate").value("2026-05-05"))
      .andExpect(jsonPath("$.state").value("active"));

  }

  @Test
  void shouldFindCustomerInAllCustomersAfterCreation() throws Exception {
    // setup
    var responseBody = mockMvc
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();
    var uuid = new ObjectMapper()
      .readTree(responseBody)
      .path("uuid")
      .asString();

    // Test
    mockMvc
      .perform(get("/customers"))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[?(@.uuid == '%s')]".formatted(uuid)).exists());

  }

  @Test
  void shouldFindCustomerInAllCustomersWithStateAfterCreation() throws Exception {
    // setup
    var responseBody = mockMvc
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();
    var uuid = new ObjectMapper()
      .readTree(responseBody)
      .path("uuid")
      .asString();

    // Test
    mockMvc
      .perform(
        get("/customers")
          .param("state", "active")
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[?(@.uuid == '%s')]".formatted(uuid)).exists());
    mockMvc
      .perform(
        get("/customers")
          .param("state", "locked")
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[?(@.uuid == '%s')]".formatted(uuid)).doesNotExist());

  }

  @Test
  void shouldNotCreateCustomerWithXmlContentType() throws Exception {
    mockMvc
      .perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_XML)
          .content("<customer/>")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isUnsupportedMediaType());
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
    // TODO Customer is not created?
  }

  @Test
  void shouldDeleteCustomerAndCustomerShouldNotExistAfterDeletion() throws Exception {
    // setup
    var responseBody = mockMvc
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
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();
    var uuid = new ObjectMapper()
      .readTree(responseBody)
      .path("uuid")
      .asString();

    // Test
    mockMvc
      .perform(delete("/customers/{uuid}", uuid))
      .andExpect(status().isNoContent());
    mockMvc
      .perform(get("/customers/{uuid}", uuid))
      .andExpect(status().isNotFound());
    mockMvc
      .perform(delete("/customers/{uuid}", uuid))
      .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @ValueSource(strings = {
    // missing comma
    """
      {
         "name": "Tom Mayer"
         "birthdate": "2005-05-12",
         "state": "active"
      }
      """,
    // invalid date
    """
      {
         "name": "Tom Mayer",
         "birthdate": "gelbekatze",
         "state": "active"
      }
      """,
    // invalid state
    """
      {
         "name": "Tom Mayer",
         "birthdate": "2005-05-12",
         "state": "gelbekatze"
      }
      """,
    // missing birthdate date
    """
      {
         "name": "Tom Mayer",
         "state": "active"
      }
      """,
    // missing name
    """
      {
         "birthdate": "2005-05-12",
         "state": "active"
      }
      """,
    // with uuid
    """
      {
         "uuid": "bf7f440b-c9de-4eb8-91f4-43108277e9a3",
         "name": "Tom Mayer",
         "birthdate": "2005-05-12",
         "state": "active"
      }
      """,
    // unknown property
    """
      {
         "name": "Tom Mayer",
         "birthdate": "2005-05-12",
         "state": "active",
         "gelbekatze": "gruenerfuchs"
      }
      """,
  })
  void shouldNotCreateInvalidCustomer(String body) throws Exception {
    mockMvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(body)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldValidateStateParameterWhenGetCustomers() throws Exception {
    mockMvc.perform(
        get("/customers")
          .param("state", "gelbekatze")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest());
  }

  /*
   * PUT /customers/{uuid} endpoint tests
   */

  @Test
  void shouldReplaceCustomer() throws Exception {

    // setup
    var responseBody = mockMvc.perform(
      post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {
            "name": "Tom Mayer",
            "birthdate": "2005-05-12",
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

    // Test
    mockMvc.perform(
      put("/customers/{uuid}", uuid)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {
            "name": "Tom Smith",
            "birthdate": "2005-05-13",
            "state": "locked"
          }
          """)
      )
      .andExpect(status().isNoContent());

    // Assertions
    mockMvc.perform(
        get("/customers/{uuid}", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value("Tom Smith"))
      .andExpect(jsonPath("$.birthdate").value("2005-05-13"))
      .andExpect(jsonPath("$.state").value("locked"));
  }

  @Test
  void shouldNotReplaceInvalidCustomer() throws Exception {
    // setup: create customer and get uuid
    var responseBody = mockMvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "name": "Tom Mayer",
                "birthdate": "2005-05-12",
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

    // Test
    mockMvc.perform(
        put("/customers/{uuid}", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "birthdate": "2006-05-12",
                "state": "locked"
              }
            """)
      )
      .andExpect(status().isBadRequest());

    // Assertion: Original resource still exists
    mockMvc.perform(
        get("/customers/{uuid}", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value("Tom Mayer"))
      .andExpect(jsonPath("$.birthdate").value("2005-05-12"))
      .andExpect(jsonPath("$.state").value("active"));

  }

  @Test
  void shouldNotReplaceMissingCustomer() throws Exception {
    // setup: create customer, get uuid and delete it
    var responseBody = mockMvc.perform(
        post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "name": "Tom Mayer",
                "birthdate": "2005-05-12",
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

    mockMvc.perform(
        delete("/customers/{uuid}", uuid)
      )
      .andExpect(status().isNoContent());

    // Test
    mockMvc.perform(
        put("/customers/{uuid}", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "name": "Tom Smith",
                "birthdate": "2006-05-12",
                "state": "locked"
              }
            """)
      )
      .andExpect(status().isNotFound());

  }

}
