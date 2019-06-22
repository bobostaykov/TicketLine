package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface CustomerService {

    /**
     * Add customer.
     * @param customerDTO customer to be added
     * @return added customer
     */
    CustomerDTO addCustomer(CustomerDTO customerDTO);

    /**
     * Change customer information.
     *
     * @param customerDTO customer to be changed
     * @return changed customer
     */
    CustomerDTO adaptCustomer(CustomerDTO customerDTO);

    /**
     * Find all customers saved in the database.
     *
     * @param page the number of the page to return
     * @return page of the found customers
     */
    Page<CustomerDTO> findAll(Integer page);

    /**
     * Find a single customer entry by id.
     *
     * @param id id of the customer entry
     * @return found customer entry
     */
    CustomerDTO findOne(Long id);

    /**
     * Find all customers filtered by the following attributes:
     * ID - ID of customer
     * name - part of the name of customer
     * firstname - part of the first name of customer
     * email - part of the e-mail address of customer
     * birthday - birthday of customer
     *
     * @param page the number of the page to return
     * @param id ID of customer to search for
     * @param name name of customer to search for
     * @param firstname first name of customer to search for
     * @param email e-mail adress of customer to search for
     * @param birthday birthday of customer to search for
     * @return page of customers that met the requested filter methods
     */
    Page<CustomerDTO> findCustomersFiltered(Long id, String name, String firstname, String email, LocalDate birthday, Integer page, Integer pageSize);
}
