package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist_;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event_;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    public EventRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override

    public List<Event> findAllEventsFiltered(EventSearchParametersDTO parameters) throws PersistenceException {

        LOGGER.info("Find Events filtered by: " + parameters.toString());
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        em.getMetamodel();
        CriteriaQuery<Event> query = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        if(parameters.getName() != null){
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get(Event_.name)), "%" + parameters.getName().toLowerCase() + "%"));
        }
        if(parameters.getContent() != null){
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get(Event_.content)), "%" + parameters.getContent().toLowerCase() + "%"));
        }

        //Current Search Area is +- 30 minutes
        if(parameters.getDurationInMinutes()!= null){
            predicates.add(criteriaBuilder.between(
                eventRoot.get(Event_.durationInMinutes), parameters.getDurationInMinutes() -30 , parameters.getDurationInMinutes() + 30));
        }

        if(parameters.getArtistName() != null){
            Join<Event, Artist> eventArtistJoin = eventRoot.join(Event_.artist);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventArtistJoin.get(Artist_.name)), "%" + parameters.getArtistName().toLowerCase() + "%"));
        }
        if(parameters.getDescription() != null){
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get(Event_.description)), "%" + parameters.getDescription().toLowerCase() + "%"));
        }
        if(parameters.getEventType() != null){
            //predicates.add(criteriaBuilder.equal(eventRoot.get(Event_.eventType), parameters.getEventType()));
            predicates.add(criteriaBuilder.equal(eventRoot.get(Event_.eventType), parameters.getEventType()));
        }


        query.select(eventRoot).where(predicates.toArray(new Predicate[predicates.size()]));
        query.orderBy(criteriaBuilder.asc(eventRoot.get(Event_.eventType))).orderBy(criteriaBuilder.asc(eventRoot.get(Event_.name)));
        List results = em.createQuery(query).getResultList();
        return results;
    }
    /*
    @Query("SELECT e.name, SUM(s.ticketsSold) FROM Show s, Event e WHERE s.event = e.id AND MONTHNAME(s.date) IN :monthsSet AND e.eventType IN :categoriesSet GROUP BY s.event ORDER BY SUM(s.ticketsSold) DESC")
     */


    @Override
    public List<Object> findTopTenEvents2(Set<String> monthsSet, Set<EventType> categoriesSet) {
/*
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Event> query = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        Root<Show> showRoot = query.from(Show.class);
        Join<Show, Event> showEventJoin = showRoot.join(Show_.event);
        for (EventType eventType: categoriesSet) {
            predicates.add(criteriaBuilder.equal(showEventJoin.get(Event_.eventType), eventType));
        }
        for (String:
             ) {

        }

 */
        //todo create topTenMethod
        return null;

    }
}
