package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location_;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    private EntityManager entityManager;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public LocationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Location> findLocationsFiltered(String name, String country, String city, String street, String postalCode, String description, Pageable pageable) {

        LOGGER.info("Location Repository Impl: findLocationsFiltered");
        LOGGER.debug("name: " + name);
        LOGGER.debug("country: " + country);
        LOGGER.debug("city: " + city);
        LOGGER.debug("street: " + street);
        LOGGER.debug("postalCode: " + postalCode);
        LOGGER.debug("description: " + description);
        LOGGER.debug("page: " + pageable.getPageNumber());
        LOGGER.debug("pageSize: " + pageable.getPageSize());

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Location> criteriaQuery = cBuilder.createQuery(Location.class);
        Root<Location> location = criteriaQuery.from(Location.class);

        if (name != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.locationName)), "%" + name.toLowerCase() + "%"));
        }
        if (country != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.country)), "%" + country.toLowerCase() + "%"));
        }
        if (city != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.city)), "%" + city.toLowerCase() + "%"));
        }
        if (street != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.street)), "%" + street.toLowerCase() + "%"));
        }
        if (postalCode != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.postalCode)), "%" + postalCode.toLowerCase() + "%"));
        }
        if (description != null) {
            predicates.add(cBuilder.like(cBuilder.lower(location.get(Location_.description)), "%" + description.toLowerCase() + "%"));
        }

        criteriaQuery.select(location).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Location> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Location> locationList = typedQuery.getResultList();
        if (locationList.isEmpty()) {
            throw new NotFoundException("No locations are found with those parameters");
        }

        int totalElements = locationList.size();

        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        locationList = typedQuery.getResultList();

        return new PageImpl<>(locationList, pageable, totalElements);
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
