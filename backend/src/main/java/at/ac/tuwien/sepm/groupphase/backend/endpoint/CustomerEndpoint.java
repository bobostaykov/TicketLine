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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific customer entry", authorizations = {@Authorization(value = "apiKey")})
    public CustomerDTO find(@PathVariable Long id) {
        return customerMapper.customerToCustomerDTO(customerService.findOne(id));
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
    @ApiOperation(value = "Get list of all customer entries filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<CustomerDTO> findAllFiltered(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "firstname", required = false) String firstname,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "birthday", required = false) String birthday_str) {

        boolean filterData = !(id == null && name == null && firstname == null && email == null && birthday_str == null);
        LocalDate birthday = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (birthday_str != null) {
            birthday = LocalDate.parse(birthday_str, formatter);
        }
        if (filterData) {
            LOGGER.info("Get all customers filtered by specified attributes");
        } else {
            LOGGER.info("Get all customers");
        }
        try {
            List<Customer> result;
            if (filterData) {
                result = customerService.findCustomersFiltered(id, name, firstname, email, birthday);
            } else {
                result = customerService.findAll();
            }
            return customerMapper.customerToCustomerDTO(result);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during reading filtered customers", e);
        }
    }
}
