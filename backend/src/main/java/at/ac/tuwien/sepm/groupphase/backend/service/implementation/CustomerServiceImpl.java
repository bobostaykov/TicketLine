package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        LOGGER.info("Add a customer");
        if (customerDTO != null) {
            // VALIDATION START
            if (customerDTO.getName() == null || customerDTO.getName().isBlank())
                throw new ServiceException("Customer " + customerDTO.toString() + "could not be added: username must not be empty");
            if (customerDTO.getFirstname() == null || customerDTO.getFirstname().isBlank())
                throw new ServiceException("Customer " + customerDTO.toString() + "could not be added: first username must not be empty");
            if (customerDTO.getEmail() == null || customerDTO.getEmail().isBlank())
                throw new ServiceException("Customer " + customerDTO.toString() + "could not be added: email must not be empty");
            if (customerDTO.getBirthday() == null)
                throw new ServiceException("Customer " + customerDTO.toString() + "could not be added: birthday must not be empty");
            //VALIDATION END
            return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO)));
        }
        else
            throw new ServiceException("Customer could not be added " + customerDTO.toString());
    }

    @Override
    public CustomerDTO findOne(Long id) {
        LOGGER.info("Find one customer by ID");
        return customerMapper.customerToCustomerDTO(customerRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public void adaptCustomer(CustomerDTO customerDTO) {
        LOGGER.info("Adapt customer: " + customerDTO.toString());
        Long id = customerDTO.getId();
        if (customerDTO.getName() != null) {
            customerRepository.updateName(customerDTO.getName(), id);
        }

        if (customerDTO.getFirstname() != null) {
            customerRepository.updateFirstname(customerDTO.getFirstname(), id);
        }
        if (customerDTO.getEmail() != null) {
            customerRepository.updateEmail(customerDTO.getEmail(), id);
        }
        if (customerDTO.getBirthday() != null) {
            customerRepository.updateBirthday(customerDTO.getBirthday(), id);
        }
    }

    @Override
    public List<CustomerDTO> findAll() {
        LOGGER.info("Find all customers ordered by ID");
        return customerMapper.customerToCustomerDTO(customerRepository.findAllByOrderByIdAsc());
    }

    @Override
    public List<CustomerDTO> findCustomersFiltered(Long id, String name, String firstname, String email, LocalDate birthday) {
        LOGGER.info("Find customers filtered");
        return customerMapper.customerToCustomerDTO(customerRepository.findCustomersFiltered(id, name, firstname, email, birthday));
    }
 }
