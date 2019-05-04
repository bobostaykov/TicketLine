package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Change the name of customer with ID id.
     *
     * @param name new name for customer
     * @param id ID of customer
     */
    @Query(value = "UPDATE customer c SET c.name = :name WHERE c.id = :id", nativeQuery = true)
    void updateName(@Param("name") String name, @Param("id") Long id);

    /**
     * Change the firstname of the customer with ID id.
     *
     * @param firstname new first name for customer
     * @param id ID of the customer
     */
    @Query(value = "UPDATE customer SET firstname = :firstname WHERE id = :id", nativeQuery = true)
    void updateFirstname(@Param("firstname") String firstname, @Param("id") Long id);

    /**
     * Change the e-mail address of the customer with ID id.
     *
     * @param email new e-mail address for customer
     * @param id ID of the customer
     */
    @Query(value = "UPDATE customer SET email = :email WHERE id = :id", nativeQuery = true)
    void updateEmail(@Param("email") String email, @Param("id") Long id);

    /**
     * Change the birthday of the customer with ID id.
     *
     * @param birthday new birthday for customer
     * @param id ID of the customer
     */
    @Query(value = "UPDATE customer SET birthday = :birthday WHERE id = :id", nativeQuery = true)
    void updateBirthday(@Param("birthday") LocalDate birthday, @Param("id") Long id);
}
