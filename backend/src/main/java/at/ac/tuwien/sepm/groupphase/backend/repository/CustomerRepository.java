package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {

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
    List<Customer> findAllByName(String surname);

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
     * @param firstname first name of customer to search for
     * @param email e-mail address of customer to search for
     * @param birthday birthday of customer to search for
     * @return page of customers that met the requested filter methods
     */
    @Query(value = "SELECT * " +
        "FROM customer c " +
        "WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%')) OR :name IS NULL) " +
        "AND (LOWER(c.firstname) LIKE LOWER(CONCAT('%',:firstname,'%')) OR :firstname IS NULL) " +
        "AND (LOWER(c.email) LIKE LOWER(CONCAT('%',:email,'%')) OR :email IS NULL) " +
        "AND (c.birthday = :birthday OR :birthday IS NULL) " +
        "AND (c.id = :id OR :id IS NULL)", nativeQuery = true)
    Page<Customer> findCustomersFiltered(@Param("id") Long id, @Param("name") String name,
                                         @Param("firstname") String firstname, @Param("email") String email,
                                         @Param("birthday") LocalDate birthday,
                                         Pageable pageable);
}
