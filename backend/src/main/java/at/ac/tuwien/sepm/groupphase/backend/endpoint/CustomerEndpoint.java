package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import at.ac.tuwien.sepm.groupphase.backend.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
@Api(value = "customers")
public class CustomerEndpoint {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHeaderTokenAuthenticationService.class);

    public CustomerEndpoint(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void adaptCustomer(@RequestBody CustomerDTO customerdto, @PathVariable("id") Long id) {
        LOGGER.info("Adapt customer " + customerdto.toString());
        Customer customer = customerMapper.customerDTOToCustomer(customerdto);
        customer.setId(id);
        try {
            customerService.adaptCustomer(customer);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating customer: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when reading customer: " + e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all customer entries", authorizations = {@Authorization(value = "apiKey")})
    public List<CustomerDTO> findAll() {
        return customerMapper.customerToCustomerDTO(customerService.findAll());
    }
}
