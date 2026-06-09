package de.schulung.spring.customers.boundary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@BoundaryTest
class StaticResourcesTests {

  @Autowired
  MockMvc mockMvc;

  /*
   * Die Anwendung hat eine Landing Page.
   */

  @Test
  void should_have_landing_page() throws Exception {
    mockMvc
      .perform(get("/"))
      .andExpect(status().isOk());
    // mit MockMvc kein vollständiger Webserver -> greift nicht
    // .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
  }

  /*
   * Das OpenAPI ist verfügbar unter /openapi.yml.
   */

  @Test
  void should_have_openapi_yml() throws Exception {
    mockMvc
      .perform(get("/openapi.yml"))
      .andExpect(status().isOk());
    // mit MockMvc kein vollständiger Webserver -> greift nicht
    //.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_YAML));
  }

}
