package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void adaptCustomer(Customer customer) {
        Long id = customer.getId();

        if (customer.getName() != null) {
            customerRepository.updateName(customer.getName(), id);
        }
        /**
        if (customer.getFirstname() != null) {
            customerRepository.updateFirstname(customer.getFirstname(), id);
        }
        if (customer.getEmail() != null) {
            customerRepository.updateEmail(customer.getEmail(), id);
        }
        if (customer.getBirthday() != null) {
            customerRepository.updateBirthday(customer.getBirthday(), id);
        }
         */
    }
}
