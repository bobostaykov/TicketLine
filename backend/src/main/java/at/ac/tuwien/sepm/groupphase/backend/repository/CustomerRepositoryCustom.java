package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomerRepositoryCustom {

    /**
     * Find all customers filtered by the following attributes:
     * ID - ID of customer
     * name - part of the name of customer
     * firstname - part of the first name of customer
     * email - part of the e-mail address of customer
     * birthday - birthday of customer
     *
     * @param pageable special parameter to apply pagination
     * @param id ID of customer to search for
     * @param name name of customer to search for
     * @param firstName first name of customer to search for
     * @param email e-mail address of customer to search for
     * @param birthday birthday of customer to search for
     * @return page of customers that met the requested filter methods
     */
    Page<Customer> findCustomersFiltered( Long id, String name, String firstName, String email, LocalDate birthday, Pageable pageable);
}
