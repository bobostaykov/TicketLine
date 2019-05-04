package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHeaderTokenAuthenticationService.class);

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findOne(Long id) {
        return customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void adaptCustomer(Customer customer) {
        LOGGER.info("Adapt customer: " + customer.toString());
        Long id = customer.getId();
        if (customer.getName() != null) {
            customerRepository.updateName(customer.getName(), id);
        }

        if (customer.getFirstname() != null) {
            customerRepository.updateFirstname(customer.getFirstname(), id);
        }
        if (customer.getEmail() != null) {
            customerRepository.updateEmail(customer.getEmail(), id);
        }
        if (customer.getBirthday() != null) {
            customerRepository.updateBirthday(customer.getBirthday(), id);
        }
    }

    @Override
    public List<Customer> findAll() {
        LOGGER.info("Find all customers ordered by ID");
        return customerRepository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Customer> findCustomersFiltered(Long id, String name, String firstname, String email, LocalDate birthday) {
        LOGGER.info("Find customers filtered");
        return customerRepository.findCustomersFiltered(id, name, firstname, email, birthday);
    }
 }
