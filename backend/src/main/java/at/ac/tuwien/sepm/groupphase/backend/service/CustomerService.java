package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;

public interface CustomerService {

    /**
     * Add customer.
     * @param customer customer to be added
     */
    Customer addCustomer(Customer customer);

    /**
     * Change customer information.
     * @param customer customer to be changed
     */
    void adaptCustomer(Customer customer);

}
