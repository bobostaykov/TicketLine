package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Find all customers and order the list by the ID ascending.
     *
     * @return list of all customers
     */
    List<Customer> findAllByOrderByIdAsc();

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
     * @param id ID of customer to search for
     * @param name name of customer to search for
     * @param firstname first name of customer to search for
     * @param email e-mail address of customer to search for
     * @param birthday birthday of customer to search for
     * @return List of customers that met the requested filter methods
     */
    @Query(value = "SELECT DISTINCT * " +
        "FROM customer c " +
        "WHERE (c.name LIKE CONCAT('%',:name,'%') OR :name IS NULL) " +
        "AND (c.firstname LIKE CONCAT('%',:firstname,'%') OR :firstname IS NULL) " +
        "AND (c.email LIKE CONCAT('%',:email,'%') OR :email IS NULL) " +
        "AND (c.birthday = :birthday OR :birthday IS NULL) " +
        "AND (c.id = :id OR :id IS NULL)", nativeQuery = true)
    List<Customer> findCustomersFiltered(@Param("id") Long id, @Param("name") String name,
                                         @Param("firstname") String firstname, @Param("email") String email,
                                         @Param("birthday") LocalDate birthday);

    /**
     * Change the name of customer with ID id.
     *
     * @param name new name for customer
     * @param id ID of customer
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Customer c SET c.name = :name WHERE c.id = :id", nativeQuery = true)
    void updateName(@Param("name") String name, @Param("id") Long id);

    /**
     * Change the firstname of the customer with ID id.
     *
     * @param firstname new first name for customer
     * @param id ID of the customer
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Customer SET firstname = :firstname WHERE id = :id", nativeQuery = true)
    void updateFirstname(@Param("firstname") String firstname, @Param("id") Long id);

    /**
     * Change the e-mail address of the customer with ID id.
     *
     * @param email new e-mail address for customer
     * @param id ID of the customer
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Customer SET email = :email WHERE id = :id", nativeQuery = true)
    void updateEmail(@Param("email") String email, @Param("id") Long id);

    /**
     * Change the birthday of the customer with ID id.
     *
     * @param birthday new birthday for customer
     * @param id ID of the customer
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Customer SET birthday = :birthday WHERE id = :id", nativeQuery = true)
    void updateBirthday(@Param("birthday") LocalDate birthday, @Param("id") Long id);
}
