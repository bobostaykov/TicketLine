package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        if (customer != null) {
            // VALIDATION START
            if (customer.getName() == null || customer.getName().isBlank())
                throw new ServiceException("Customer " + customer.toString() + "could not be added: name must not be empty");
            if (customer.getFirstname() == null || customer.getFirstname().isBlank())
                throw new ServiceException("Customer " + customer.toString() + "could not be added: first name must not be empty");
            if (customer.getEmail() == null || customer.getEmail().isBlank())
                throw new ServiceException("Customer " + customer.toString() + "could not be added: email must not be empty");
            if (customer.getBirthday() == null)
                throw new ServiceException("Customer " + customer.toString() + "could not be added: birthday must not be empty");
            //VALIDATION END
            return customerRepository.save(customer);
        }
        else
            throw new ServiceException("Customer could not be added " + customer.toString());
    }

    @Override
    public void adaptCustomer(Customer customer) {
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
}
