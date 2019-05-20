package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    //List<Location> findLocationsByCountryAndCityAndPostalCodeAndStreetAndDescriptionContainingOrderByCountry(String country, String city, String street, String postalCode, String description) throws NotFoundException;

    @Query(value = "SELECT DISTINCT * " +
        "FROM location l " +
        "WHERE (l.country = :country OR :country IS NULL) " +
        "AND (l.city = :city OR :city IS NULL) " +
        "AND (l.street LIKE CONCAT('%',:street,'%') OR :street IS NULL) " +
        "AND (l.postalCode = :postalCode OR :postalCode IS NULL) " +
        "AND (l.description LIKE CONCAT('%',:description,'%') OR :description IS NULL)", nativeQuery = true)
    List<Location> findLocationsFiltered(@Param("country") String country, @Param("city") String city,
                                         @Param("street") String street, @Param("postalCode") String postalCode,
                                         @Param("description") String description) throws NotFoundException;

}

