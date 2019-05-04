package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;

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

}
