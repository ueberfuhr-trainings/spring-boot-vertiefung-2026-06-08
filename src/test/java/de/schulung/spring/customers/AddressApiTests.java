package de.schulung.spring.customers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomersApplicationTest
public class AddressApiTests {

  @Autowired
  MockMvc mockMvc;

  private static final String VALID_ADDRESS_JSON = """
    {
      "street": "Hauptstrasse",
      "number": "1a",
      "zip": "12345",
      "city": "Musterstadt"
    }
    """;

  private String createCustomer() throws Exception {
    var responseBody = mockMvc
      .perform(
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
    return JsonPath.read(responseBody, "$.uuid");
  }

  /*
   * PUT /customers/{uuid}/address (initiale Zuweisung) -> 201 Created
   */
  @Test
  void shouldAssignAddressInitiallyReturn201() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(VALID_ADDRESS_JSON)
      )
      .andExpect(status().isCreated());
  }

  /*
   * GET /customers/{uuid}/address nach PUT -> 200, Array enthält die Adresse
   */
  @Test
  void shouldReturnAssignedAddressOnGet() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(VALID_ADDRESS_JSON)
      )
      .andExpect(status().isCreated());

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].street").value("Hauptstrasse"))
      .andExpect(jsonPath("$[0].number").value("1a"))
      .andExpect(jsonPath("$[0].zip").value("12345"))
      .andExpect(jsonPath("$[0].city").value("Musterstadt"));
  }

  /*
   * Zweites PUT überschreibt eine bestehende Adresse -> 204 No Content.
   */
  @Test
  void shouldReplaceAddressOnPut() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(VALID_ADDRESS_JSON)
      )
      .andExpect(status().isCreated());

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "street": "Bahnhofstrasse",
              "number": "42",
              "zip": "54321",
              "city": "Neustadt"
            }
            """)
      )
      .andExpect(status().isNoContent());

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].street").value("Bahnhofstrasse"))
      .andExpect(jsonPath("$[0].number").value("42"))
      .andExpect(jsonPath("$[0].zip").value("54321"))
      .andExpect(jsonPath("$[0].city").value("Neustadt"));
  }

  /*
   * PUT ohne optionales "number" -> 201 (initiale Zuweisung)
   */
  @Test
  void shouldAssignAddressWithoutOptionalNumber() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "street": "Hauptstrasse",
              "zip": "12345",
              "city": "Musterstadt"
            }
            """)
      )
      .andExpect(status().isCreated());

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].street").value("Hauptstrasse"))
      .andExpect(jsonPath("$[0].zip").value("12345"))
      .andExpect(jsonPath("$[0].city").value("Musterstadt"));
  }

  /*
   * GET /customers/{uuid}/address ohne zugewiesene Adresse -> 404
   */
  @Test
  void shouldGetAddressReturn404WhenNoneAssigned() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNotFound());
  }

  /*
   * GET /customers/{uuid}/address für unbekannten Customer -> 404
   */
  @Test
  void shouldGetAddressReturn404ForUnknownCustomer() throws Exception {
    final var unknownUuid = UUID.randomUUID();

    mockMvc
      .perform(
        get("/customers/{uuid}/address", unknownUuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNotFound());
  }

  /*
   * PUT /customers/{uuid}/address für unbekannten Customer -> 404
   */
  @Test
  void shouldPutAddressReturn404ForUnknownCustomer() throws Exception {
    final var unknownUuid = UUID.randomUUID();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", unknownUuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(VALID_ADDRESS_JSON)
      )
      .andExpect(status().isNotFound());
  }

  /*
   * GET /customers/{uuid}/address mit Accept: application/xml -> 406
   */
  @Test
  void shouldGetAddressAsXmlReturnNotAcceptable() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_XML)
      )
      .andExpect(status().isNotAcceptable());
  }

  /*
   * PUT mit Content-Type XML -> 415 Unsupported Media Type
   */
  @Test
  void shouldNotAssignAddressWithXmlContentType() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_XML)
          .content("<address/>")
      )
      .andExpect(status().isUnsupportedMediaType());
  }

  /*
   * Nach PUT mit ungültiger Adresse bleibt eine zuvor zugewiesene Adresse unverändert.
   */
  @Test
  void shouldNotReplaceValidAddressWithInvalidOne() throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(VALID_ADDRESS_JSON)
      )
      .andExpect(status().isCreated());

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {
              "street": "Bahnhofstrasse",
              "zip": "ABCDE",
              "city": "Neustadt"
            }
            """)
      )
      .andExpect(status().isBadRequest());

    mockMvc
      .perform(
        get("/customers/{uuid}/address", uuid)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].street").value("Hauptstrasse"))
      .andExpect(jsonPath("$[0].zip").value("12345"))
      .andExpect(jsonPath("$[0].city").value("Musterstadt"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
    // missing street
    """
      {
        "zip": "12345",
        "city": "Musterstadt"
      }
      """,
    // missing zip
    """
      {
        "street": "Hauptstrasse",
        "city": "Musterstadt"
      }
      """,
    // missing city
    """
      {
        "street": "Hauptstrasse",
        "zip": "12345"
      }
      """,
    // invalid zip - too short
    """
      {
        "street": "Hauptstrasse",
        "zip": "1234",
        "city": "Musterstadt"
      }
      """,
    // invalid zip - too long
    """
      {
        "street": "Hauptstrasse",
        "zip": "123456",
        "city": "Musterstadt"
      }
      """,
    // invalid zip - non-numeric
    """
      {
        "street": "Hauptstrasse",
        "zip": "ABCDE",
        "city": "Musterstadt"
      }
      """,
    // street too short (< 3)
    """
      {
        "street": "Ha",
        "zip": "12345",
        "city": "Musterstadt"
      }
      """,
    // city empty (< 1)
    """
      {
        "street": "Hauptstrasse",
        "zip": "12345",
        "city": ""
      }
      """,
    // invalid JSON (missing comma)
    """
      {
        "street": "Hauptstrasse"
        "zip": "12345",
        "city": "Musterstadt"
      }
      """,
    // unknown property
    """
      {
        "street": "Hauptstrasse",
        "zip": "12345",
        "city": "Musterstadt",
        "gelbekatze": "gruenerfuchs"
      }
      """,
  })
  void shouldNotAssignInvalidAddress(String body) throws Exception {
    final var uuid = createCustomer();

    mockMvc
      .perform(
        put("/customers/{uuid}/address", uuid)
          .contentType(MediaType.APPLICATION_JSON)
          .content(body)
      )
      .andExpect(status().isBadRequest());
  }

}
