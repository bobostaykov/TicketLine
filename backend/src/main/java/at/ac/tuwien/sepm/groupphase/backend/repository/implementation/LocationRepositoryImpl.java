package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location_;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PersistenceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    private EntityManager entityManager;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public LocationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Location> findLocationsFiltered(String country, String city, String street, String postalCode, String description) {

        LOGGER.info("Location Repository Impl: findLocationsFiltered");

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Location> criteriaQuery = cBuilder.createQuery(Location.class);
        Root<Location> location = criteriaQuery.from(Location.class);

        if (country != null) {
            predicates.add(cBuilder.equal(location.get(Location_.country), country));
        }
        if (city != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.city)), city.toLowerCase()));
        }
        if (street != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.street)), "%" + street + "%".toLowerCase()));
        }
        if (postalCode != null) {
            predicates.add(cBuilder.equal(cBuilder.lower(location.get(Location_.postalCode)), postalCode));
        }
        if (description != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.description)), "%" + description + "%"));
        }

        criteriaQuery.select(location).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Location> typedQuery = entityManager.createQuery(criteriaQuery);

        LOGGER.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Location> results = typedQuery.getResultList();
        if (results.isEmpty()) {
            throw new NotFoundException("No locations are found with those parameters");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getCountriesOrderedByName() throws NotFoundException {
        LOGGER.info("Location Repository Impl: getCountriesOrderedByName");
        List<String> countries = (List<String>) entityManager.createNativeQuery("SELECT DISTINCT l.country FROM Location l ORDER BY l.country").getResultList();
        if (countries == null) {
            throw new NotFoundException("Could not find locations to get countries");
        }
        return countries;
    }
}
