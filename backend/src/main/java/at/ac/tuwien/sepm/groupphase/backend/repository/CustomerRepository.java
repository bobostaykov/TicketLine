package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CustomerRepositoryCustom, JpaRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {

    /**
     * Find all customers and order the list by the ID ascending.
     *
     * @param pageable special parameter to apply pagination
     * @return a page of the found customers
     */
    Page<Customer> findAllByOrderByIdAsc(Pageable pageable);

    /**
     * Find all customers by surname
     *
     * @param surname name of customer
     * @return list of customers found
     */
    List<Customer> findAllByNameContainsIgnoreCase(String surname);

    /**
     * Find all customers by firstname
     *
     * @param firstname name of customer
     * @return list of customers found
     */
    List<Customer> findAllByFirstname(String firstname);

    /**
     * Find a single customer entry by id.
     *
     * @param id id of the customer entry
     * @return Optional containing the found customer entry
     */
    Optional<Customer> findOneById(Long id);

}
