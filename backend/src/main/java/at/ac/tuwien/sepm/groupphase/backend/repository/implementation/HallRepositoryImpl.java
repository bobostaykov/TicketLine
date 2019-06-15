package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.HallRequestParameters;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class HallRepositoryImpl implements HallRepositoryCustom {

    private final EntityManager em;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public List<Hall> findAllWithSpecifiedFields(List<HallRequestParameters> fields) {
        LOGGER.info("Retrieve all halls with specified fields: " + fields.toString());
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> hallQuery = criteriaBuilder.createTupleQuery();
        Root<Hall> hallRoot = hallQuery.from(Hall.class);
        List<Selection<?>> selections = new ArrayList<>();
        for(HallRequestParameters field : fields) {
            selections.add(hallRoot.get(field.name().toLowerCase()));
        }
        hallQuery.multiselect(selections);
        List<Tuple> tupleList = em.createQuery(hallQuery).getResultList();
        List<Hall> hallList = new ArrayList<>();
        // TODO: Wirklich schlechte Lösung. Tutor/Gruppe noch fragen und hier was besseres finden
        for(Tuple tuple : tupleList) {
            Hall hall = new Hall();
            for(int i = 0; i < fields.size(); i++) {
                switch (fields.get(i)){
                    case ID:
                        hall.setId((Long) tuple.get(i));
                        break;
                    case NAME:
                        hall.setName((String) tuple.get(i));
                        break;
                    case LOCATION:
                        hall.setLocation((Location) tuple.get(i));
                        break;
                }
            }
            hallList.add(hall);
        }
        return hallList;
    }
}
