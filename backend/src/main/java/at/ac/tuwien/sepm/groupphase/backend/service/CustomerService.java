package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    /**
     * Change customer information.
     *
     * @param customer customer to be changed
     */
    void adaptCustomer(Customer customer);

    /**
     * Find all customers saved in the database.
     *
     * @return list of all customers
     */
    List<Customer> findAll();

    /**
     * Find a single customer entry by id.
     *
     * @param id id of the customer entry
     * @return found customer entry
     */
    Customer findOne(Long id);

    /**
     * Find all customers filtered by the following attributes:
     * ID - ID of customer
     * name - part of the name of customer
     * firstname - part of the first name of customer
     * email - part of the e-mail address of customer
     * birthday - birthday of customer
     *
     * @param id ID of customer to search for
     * @param name name of customer to search for
     * @param firstname first name of customer to search for
     * @param email e-mail adress of customer to search for
     * @param birthday birthday of customer to search for
     * @return List of customers that met the requested filter methods
     */
    List<Customer> findCustomersFiltered(Long id, String name, String firstname, String email, LocalDate birthday);
}
