package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShowRepositoryImpl implements ShowRepositoryCustom {
    EntityManager em;

    @Autowired
    public ShowRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Show> findAllShowsFiltered(ShowSearchParametersDTO parameters) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        //Sammlung der Bedingungen
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Show> cq = cBuilder.createQuery(Show.class);Root<Show> show = cq.from(em.getMetamodel().entity(Show.class));
        if(parameters.getDateFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get("date"), parameters.getDateFrom()));
        }
        if(parameters.getDateTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get("date"), parameters.getDateTo()));
        }
        if (parameters.getTimeFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get("time"), parameters.getTimeFrom()));
        }
        if (parameters.getTimeTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get("time"), parameters.getTimeTo()));
        }
        if(parameters.getPriceInEuroFrom() != null){
            predicates.add(cBuilder.greaterThanOrEqualTo(show.get("price"), parameters.getPriceInEuroFrom()));
        }
        if(parameters.getPriceInEuroTo() != null){
            predicates.add(cBuilder.lessThanOrEqualTo(show.get("price"), parameters.getPriceInEuroTo()));
        }
        /*
        if(parameters.getEventName() != null){
            predicates.add(cBuilder.like(show.<String>get("event")))
        }
        if(parameters.getHallName()) != null{

        }
        */
        //Übergabe der Predicates
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.select(show);
        return em.createQuery(cq).getResultList();


    }
}
