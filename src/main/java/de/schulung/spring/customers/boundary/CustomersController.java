package de.schulung.spring.customers.boundary;

import de.schulung.spring.customers.domain.CustomersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

  private final CustomersService customersService;
  private final CustomerDtoMapper mapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Stream<CustomerDto> getCustomers(
    @RequestParam(value = "state", required = false)
    @Pattern(regexp = "active|locked|disabled")
    String stateFilter
  ) {
    return
      (
        stateFilter == null
          ? customersService.getCustomers()
          : customersService.getCustomersByState(mapper.mapState(stateFilter))
      ).map(mapper::map);

  }

  @GetMapping(
    path = "/{uuid}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public CustomerDto findCustomerById(@PathVariable UUID uuid) {
    return customersService
      .getCustomerByUuid(uuid)
      .map(mapper::map)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  // @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {

    var customer = mapper.map(customerDto);
    customersService.createCustomer(customer);
    var responseDto = mapper.map(customer);

    final var location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{uuid}")
      .buildAndExpand(responseDto.getUuid())
      .toUri();

    return ResponseEntity
      .created(location)
      .body(responseDto);

  }

  @DeleteMapping("/{uuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCustomer(@PathVariable UUID uuid) {
    if (!customersService.existsCustomerByUuid(uuid)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    customersService.deleteCustomer(uuid);
  }

  @PutMapping(
    path = "/{uuid}",
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void replaceCustomer(@PathVariable UUID uuid, @RequestBody CustomerDto customerDto) {
    customerDto.setUuid(uuid);
    customersService.replaceCustomer(mapper.map(customerDto));
  }

}
