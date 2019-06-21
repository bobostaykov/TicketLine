package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShowRepositoryImpl implements ShowRepositoryCustom {
    EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShowRepositoryImpl(EntityManager em) {
        this.em = em;
    }



    @Override
    public Page<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters, Integer page) {

        LOGGER.info("Find shows filtered by " + parameters.toString());

        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        //Sammlung der Bedingungen
        List<Predicate> predicates = new ArrayList<>();
        em.getMetamodel();
        CriteriaQuery<Show> criteriaQuery = cBuilder.createQuery(Show.class);
        Root<Show> show = criteriaQuery.from(Show.class);

        // with possibility to default to today to show only coming events
        if (parameters.getDateFrom() != null) {
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.date), parameters.getDateFrom()));
        }
        //}else{
        //   predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.date), LocalDate.now()));
        //}

        if (parameters.getDateTo() != null) {
            predicates.add(cBuilder.lessThanOrEqualTo(show.get(Show_.date), parameters.getDateTo()));
        }
        if (parameters.getTimeFrom() != null) {
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.time), parameters.getTimeFrom()));
        }
        if (parameters.getTimeTo() != null) {
            predicates.add(cBuilder.lessThanOrEqualTo(show.get(Show_.time), parameters.getTimeTo()));
        }


        if (parameters.getEventId() != null
            || parameters.getEventName() != null
            || (parameters.getDurationInMinutes() != null && parameters.getDurationInMinutes() != 0)) {
            Join<Show, Event> eventJoin = show.join(Show_.event);
            if (parameters.getEventId() != null) {
                predicates.add(cBuilder.equal(eventJoin.get(Event_.id), parameters.getEventId()));
            }

            if (parameters.getEventName() != null) {
                ;
                predicates.add(cBuilder.like(cBuilder.lower(eventJoin.get(Event_.name)), "%" + parameters.getEventName().toLowerCase() + "%"));
            }

            if (parameters.getDurationInMinutes() != null && parameters.getDurationInMinutes() != 0) {
                predicates.add(cBuilder.between
                    (eventJoin.get(Event_.durationInMinutes), parameters.getDurationInMinutes() - 30, parameters.getDurationInMinutes() + 30));
            }
        }


        //All parameters that need Join on hall
        if (parameters.getCity() != null
            || parameters.getCountry() != null
            || parameters.getPostalCode() != null
            || parameters.getStreet() != null
            || (parameters.getHallName() != null && !parameters.getHallName().isBlank())
            || (parameters.getLocationName() != null && !parameters.getLocationName().isBlank())) {

            Join<Show, Hall> showHallJoin = show.join(Show_.hall);

            if (parameters.getHallName() != null && !parameters.getHallName().isBlank()) {
                predicates.add(cBuilder.like(cBuilder.lower(showHallJoin.get(Hall_.name)), "%" + parameters.getHallName().toLowerCase() + "%"));
            }

            //parameters that need join further on location
            if (parameters.getCity() != null
                || parameters.getCountry() != null
                || parameters.getPostalCode() != null
                || parameters.getStreet() != null
                || (parameters.getLocationName() != null && !parameters.getLocationName().isBlank())) {

                Join<Hall, Location> showLocationJoin = showHallJoin.join(Hall_.location);
                if (parameters.getCity() != null) {
                    predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.city))), "%" + parameters.getCity().toLowerCase() + "%"));
                }
                if (parameters.getCountry() != null) {
                    predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.country))), "%" + parameters.getCountry().toLowerCase() + "%"));
                }
                if (parameters.getStreet() != null) {
                    predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.street))), "%" + parameters.getStreet().toLowerCase() + "%"));
                }
                if (parameters.getPostalCode() != null) {
                    predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.postalCode))),
                        "%" + parameters.getPostalCode().toLowerCase() + "%"));
                }
                if (parameters.getLocationName() != null && !parameters.getLocationName().isBlank()) {
                    predicates.add(cBuilder.like(cBuilder.lower(showLocationJoin.get(Location_.locationName)), "%" + parameters.getLocationName().toLowerCase() + "%"));
                }
            }
        }

/*
        if(parameters.getPriceInEuroFrom() != null || parameters.getPriceInEuroTo() != null){
            Join<Show, PricePattern> pricePatternJoin = show.join(Show_.PRICE_PATTERN);
            MapJoin<Show, PriceCategory, Double> mapJoin = pricePatternJoin.joinMap(PricePattern_.PRICE_MAPPING);
            if(parameters.getPriceInEuroFrom() != null){
                predicates.add(cBuilder.greaterThanOrEqualTo(mapJoin.in, parameters.getPriceInEuroFrom()));
            }
            if(parameters.getPriceInEuroTo() != null){
                predicates.add(cBuilder.lessThanOrEqualTo(show.get("price"), parameters.getPriceInEuroTo()));
            } }
*/


        //Übergabe der Predicates
        criteriaQuery.select(show).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Show> typedQuery = em.createQuery(criteriaQuery);
        List<Show> showList = typedQuery.getResultList();

        //Filtern nach Preisen
        if ((parameters.getPriceInEuroFrom() != null || parameters.getPriceInEuroTo() != null) && !showList.isEmpty()) {
            if (parameters.getPriceInEuroFrom() != null) {
                showList = showList.stream()
                    .filter(compareMinPrice(parameters.getPriceInEuroFrom().doubleValue()))
                    .collect(Collectors.toList());
            }
            if (parameters.getPriceInEuroTo() != null) {
                showList = showList.stream()
                    .filter(compareMaxPrice(parameters.getPriceInEuroTo().doubleValue()))
                    .collect(Collectors.toList());
            }
        }

        //Sortieren
        Comparator<Show> byDate = Comparator.comparing(s -> s.getDate() );
        Comparator<Show> byTime = Comparator.comparing(s -> s.getTime());
        Comparator<Show> byId = Comparator.comparing(s -> s.getId());
        showList = showList.stream()
            .sorted(byDate.thenComparing(byTime).thenComparing(byId)).collect(Collectors.toList());


        // Filters from the result only the wanted page
        int pageSize = 10;
        int totalElements = showList.size();

        typedQuery.setFirstResult(page * pageSize);
        typedQuery.setMaxResults(pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        showList = typedQuery.getResultList();

        return new PageImpl<>(showList, pageable, totalElements);
    }

    public List<Show> findByEventNameAndShowDateAndShowTime(String eventName, String date, String time) {
        LOGGER.info("Find shows filtered by eventName = " + eventName + " date containing \"" + date + "\" and " +
            "time containing \"" + time + '\"');
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        List<Order> orderBy = new ArrayList<>();
        Root<Show> showRoot = query.from(Show.class);
        Join<Show, Event> eventJoin = showRoot.join(Show_.event);
        if (!StringUtils.isEmpty(eventName)) {
            predicates.add(cb.like(cb.lower(eventJoin.get(Event_.name)), '%' + eventName.trim().toLowerCase() + '%'));
            orderBy.add(cb.asc(eventJoin.get(Event_.name)));
        }
        if (!StringUtils.isEmpty(date)) {
            predicates.add(cb.like(showRoot.get(Show_.DATE).as(String.class), '%' + date.trim() + '%'));
            orderBy.add(cb.asc(showRoot.get(Show_.DATE)));
        }
        if (!StringUtils.isEmpty(time)) {
            predicates.add(cb.like(showRoot.get(Show_.TIME).as(String.class), '%' + time.trim() + '%'));
            orderBy.add(cb.asc(showRoot.get(Show_.TIME)));
        }
        query.multiselect(showRoot.get(Show_.id), eventJoin.get(Event_.name), showRoot.get(Show_.date), showRoot.get(Show_.time))
            .where(predicates.toArray(new Predicate[predicates.size()])).orderBy(orderBy);
        return em.createQuery(query).getResultStream()
            .map(result -> {
                return Show.builder()
                    .id((Long) result.get(0))
                    .event(
                        Event.builder()
                            .name((String) result.get(1))
                            .build()
                    )
                    .date((LocalDate) result.get(2))
                    .time((LocalTime) result.get(3))
                    .build();
            }).collect(Collectors.toList());
    }

    private static java.util.function.Predicate<Show> compareMaxPrice(Double maxPrice){
        return show -> show.getPricePattern()
            .getPriceMapping()
            .values()
            .stream()
            .max(Comparator
                .comparingDouble(Double::doubleValue))
            .get() > maxPrice;
    }
    private static java.util.function.Predicate<Show> compareMinPrice(Double minPrice){
        return show -> show.getPricePattern()
            .getPriceMapping()
            .values()
            .stream()
            .max(Comparator
                .comparingDouble(Double :: doubleValue))
            .get() > minPrice;
    }
}


