package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
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
        if (customerDTO != null) {
            LOGGER.info("Add customer " + customerDTO.toString());
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
        else {
            LOGGER.info("Add customer failed");
            throw new ServiceException("Customer could not be added " + customerDTO.toString());
        }
    }

    @Override
    public CustomerDTO findOne(Long id) {
        LOGGER.info("Find one customer by ID");
        return customerMapper.customerToCustomerDTO(customerRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public CustomerDTO adaptCustomer(CustomerDTO customerDTO) {
        LOGGER.info("Adapt customer: " + customerDTO.toString());
        return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO)));
    }

    @Override
    public Page<CustomerDTO> findAll(Integer page) {
        LOGGER.info("Find all customers ordered by ID");
        try{
            int pageSize = 10;
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return customerRepository.findAllByOrderByIdAsc(pageable).map(customerMapper::customerToCustomerDTO);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<CustomerDTO> findCustomersFiltered(Long id, String name, String firstname, String email, LocalDate birthday, Integer page, Integer pageSize) {
        LOGGER.info("Find customers filtered");
        try{
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            return customerRepository.findCustomersFiltered(id, name, firstname, email, birthday, pageable).map(customerMapper::customerToCustomerDTO);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage());
        }
    }
 }
