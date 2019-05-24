package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LocationRepositoryImpl implements LocationRepositoryCustom{

    private EntityManager entityManager;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public LocationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Location> findLocationsFiltered(String country, String city, String street, String postalCode, String description) {
        LOGGER.info("Location Criteria Builder: findLocationsFiltered");

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Location> criteriaQuery = cBuilder.createQuery(Location.class);
        Root<Location> location = criteriaQuery.from(Location.class);

        if(country != null){
            predicates.add(cBuilder.equal(location.get("country"), country));
        }
        if(city != null){
            predicates.add(cBuilder.like(cBuilder.lower(location.get("city")), city.toLowerCase()));
        }
        if(street != null){
            predicates.add(cBuilder.like(cBuilder.lower(location.get("street")), "%" + street + "%".toLowerCase()));
        }
        if(postalCode != null){
            predicates.add(cBuilder.equal(cBuilder.lower(location.get("postalCode")), postalCode));
        }
        if(description != null){
            predicates.add(cBuilder.like(cBuilder.lower(location.get("description")), "%" + description + "%"));
        }

        criteriaQuery.select(location).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Location> typedQuery = entityManager.createQuery(criteriaQuery);

        LOGGER.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Location> results = typedQuery.getResultList();
        if(results.isEmpty()) {
            throw new NotFoundException("No locations are found with those parameters");
        }

        return results;
    }
}
