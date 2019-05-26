package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShowRepositoryImpl implements ShowRepositoryCustom {
    EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ShowRepositoryImpl(EntityManager em) {
        this.em = em;
    }



    @Override
    public List<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters) {
/*
        LOGGER.info("find shows filtered by " + parameters.toString());
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        //Sammlung der Bedingungen
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Show> criteriaQuery = cBuilder.createQuery(Show.class);
        Root<Show> show = criteriaQuery.from(Show.class);
             //cq.from(em.getMetamodel().entity(Show.class));

        //defaults to today to show only coming events
        if(parameters.getDateFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.date), parameters.getDateFrom()));
        }else{
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.date), LocalDate.now()));
        }

        if(parameters.getDateTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get(Show_.date), parameters.getDateTo()));
        }
        if (parameters.getTimeFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.time), parameters.getTimeFrom()));
        }
        if (parameters.getTimeTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get(Show_.time), parameters.getTimeTo()));
        }


        if(parameters.getHallName() != null){
            Join<Show, Hall> halls = show.join(Show_.hall);
            predicates.add(cBuilder.like(cBuilder.lower(halls.get(Hall_.name)),"%" + parameters.getHallName().toLowerCase() + "%"));
        }
        if(parameters.getEventName() != null){
            Join<Show, Event> event = show.join(Show_.event);
            predicates.add(cBuilder.like(cBuilder.lower(event.get(Event_.name)), "%" + parameters.getEventName().toLowerCase() + "%"));
        }

        if(parameters.getDurationInMinutes() != null){
            Join<Show,Event> eventJoin = show.join(Show_.event);
            predicates.add(cBuilder.between(eventJoin.get(Event_.durationInMinutes), parameters.getDurationInMinutes() -30 , parameters.getDurationInMinutes() + 30));
        }

        if(parameters.getCity() != null || parameters.getCountry() != null || parameters.getPostalCode() != null || parameters.getStreet() != null){
            Join<Show, Hall> showHallJoin = show.join(Show_.hall);
            Join<Hall, Location> showLocationJoin = showHallJoin.join(Hall_.location);
            if(parameters.getCity() != null){
                predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.city))), "%" + parameters.getCity().toLowerCase() + "%"));
            }
            if(parameters.getCountry() != null){
                predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.country))), "%" + parameters.getCountry().toLowerCase() + "%"));
            }
            if(parameters.getStreet() != null){
                predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.street))), "%" + parameters.getStreet().toLowerCase() + "%"));
            }
            if(parameters.getPostalCode() != null){
                predicates.add(cBuilder.like((cBuilder.lower(showLocationJoin.get(Location_.postalCode))), "%" + parameters.getPostalCode().toLowerCase() + "%"));
            }
        }

 */
        /*
        if(parameters.getPriceInEuroFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get(Show_.price), parameters.getPriceInEuroFrom()));
        }
        if(parameters.getPriceInEuroTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get("price"), parameters.getPriceInEuroTo()));
        }
        */
        /*
        //Übergabe der Predicates
        criteriaQuery.select(show).where(predicates.toArray(new Predicate[predicates.size()]));
        criteriaQuery
            .orderBy(cBuilder.asc(show.get(Show_.date)))
            .orderBy(cBuilder.asc(show.get(Show_.time)))
            .orderBy(cBuilder.asc(show.get(Show_.time)))
            .orderBy(cBuilder.asc(show.get(Show_.id)));
        List results = em.createQuery(criteriaQuery).getResultList();
        return results;

         */
        return null;

    }
}
