package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist_;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event_;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    public EventRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override

    public Page<Event> findAllEventsFiltered(EventSearchParametersDTO parameters, Pageable pageable) throws PersistenceException {

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

        TypedQuery typedQuery = em.createQuery(query);
        List<Event> eventList = typedQuery.getResultList();
        if (eventList.isEmpty()) {
            throw new NotFoundException("No events are found with those parameters");
        }

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > eventList.size() ? eventList.size() : (start + pageable.getPageSize());
        Page<Event> pages = new PageImpl<Event>(eventList.subList(start, end), pageable, eventList.size());
        return pages;
    }

}
