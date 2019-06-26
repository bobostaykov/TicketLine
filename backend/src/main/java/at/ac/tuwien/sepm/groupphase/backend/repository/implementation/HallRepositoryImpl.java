package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.HallRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.HallSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall_;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HallRepositoryImpl implements HallRepositoryCustom {

    private final EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Hall> findAllWithSpecifiedFields(List<HallRequestParameter> fields) {
        LOGGER.info("Retrieve all halls returning specified fields: " + fields.toString());
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> hallQuery = criteriaBuilder.createTupleQuery();
        Root<Hall> hallRoot = hallQuery.from(Hall.class);
        List<Selection<?>> selections = createSelectionsFromRequestParameters(hallRoot, fields);
        hallQuery.multiselect(selections);
        return em.createQuery(hallQuery).getResultStream()
            .map(result -> createHallFromQueryResult(result, fields))
            .collect(Collectors.toList());
    }

    @Override
    public List<Hall> findAllFilteredWithSpecifiedFields(HallSearchParametersDTO searchParameters, List<HallRequestParameter> fields) {
        LOGGER.info("Find halls filtered by " + searchParameters.toString() + " returning specified fields " + fields);
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Tuple> hallQuery = criteriaBuilder.createTupleQuery();
        Root<Hall> hallRoot = hallQuery.from(Hall.class);
        List<Selection<?>> selections = createSelectionsFromRequestParameters(hallRoot, fields);
        List<Order> orderList = new ArrayList<>();
        if (fields.contains(HallRequestParameter.NAME)) {
            orderList.add(criteriaBuilder.asc(criteriaBuilder.length(hallRoot.get(Hall_.name))));
            orderList.add(criteriaBuilder.asc(hallRoot.get(Hall_.name)));
        } else {
            orderList.add(criteriaBuilder.asc(hallRoot.get("id")));
        }
        hallQuery.orderBy(fields.contains(HallRequestParameter.NAME) ? criteriaBuilder.asc(hallRoot.get(Hall_.name)) :
            criteriaBuilder.asc(hallRoot.get(Hall_.ID)));
        if (!StringUtils.isEmpty(searchParameters.getName())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(hallRoot.get("name")), '%' + searchParameters.getName().toLowerCase().trim() + '%'));
        }
        if (searchParameters.getLocation() != null) {
            predicates.add(criteriaBuilder.equal(hallRoot.get("location"), searchParameters.getLocation()));
        }
        hallQuery.multiselect(selections).where(predicates.toArray(new Predicate[predicates.size()])).orderBy(orderList);
        return em.createQuery(hallQuery).getResultStream()
            .map(result -> createHallFromQueryResult(result, fields))
            .collect(Collectors.toList());
    }

    private List<Selection<?>> createSelectionsFromRequestParameters(Root<Hall> hallRoot, List<HallRequestParameter> fields) {
        List<Selection<?>> selections = new ArrayList<>();
        for (HallRequestParameter field : fields) {
            selections.add(hallRoot.get(field.name().toLowerCase()));
        }
        return selections;
    }

    private Hall createHallFromQueryResult(Tuple result, List<HallRequestParameter> fields) {
        Hall hall = new Hall();
        for (int i = 0; i < fields.size(); i++) {
            switch (fields.get(i)) {
                case ID:
                    hall.setId((Long) result.get(i));
                    break;
                case NAME:
                    hall.setName((String) result.get(i));
                    break;
                case LOCATION:
                    hall.setLocation((Location) result.get(i));
                    break;
            }
        }
        return hall;
    }
}
