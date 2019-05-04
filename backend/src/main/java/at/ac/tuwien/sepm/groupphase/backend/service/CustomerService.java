package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;

public interface CustomerService {
    /**
     * Change customer information.
     * @param customer customer to be changed
     */
    void adaptCustomer(Customer customer);

}
