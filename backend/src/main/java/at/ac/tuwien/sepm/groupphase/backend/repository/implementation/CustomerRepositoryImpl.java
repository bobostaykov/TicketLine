package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer_;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
    @PersistenceContext
    private final EntityManager entityManager;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public CustomerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Customer> findCustomersFiltered(Long id, String name, String firstName, String email, LocalDate birthday, Pageable pageable) {
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Customer> criteriaQuery = cBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = criteriaQuery.from(Customer.class);

        if(id != null){
            predicates.add(cBuilder.equal(customerRoot.get(Customer_.id), id));
        }
        if(name != null){
            predicates.add(cBuilder.like(cBuilder.lower(customerRoot.get(Customer_.name)), "%" + name.toLowerCase() + "%"));
        }
        if(firstName != null){
            predicates.add(cBuilder.like(cBuilder.lower(customerRoot.get(Customer_.firstname)), "%" + firstName.toLowerCase() + "%"));
        }
        if(email != null){
            predicates.add(cBuilder.like(cBuilder.lower(customerRoot.get(Customer_.email)), "%" + email.toLowerCase() + "%"));
        }
        if(birthday != null){
            predicates.add(cBuilder.equal(customerRoot.get(Customer_.birthday), birthday));
        }
        //order
        criteriaQuery.orderBy(cBuilder.asc(customerRoot.get(Customer_.id))).orderBy(cBuilder.asc(customerRoot.get(Customer_.name))).orderBy(cBuilder.asc(customerRoot.get(Customer_.firstname)));
        //excecute Query
        criteriaQuery.select(customerRoot).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Customer> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Customer> customerList = typedQuery.getResultList();

        if(customerList.isEmpty()){
            throw new NotFoundException("no customers matching the criteria found");
        }
        //filter the required page
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > customerList.size() ? customerList.size() : (start + pageable.getPageSize());
        Page<Customer> pages = new PageImpl<Customer>(customerList.subList(start, end), pageable, customerList.size());
        return pages;
    }
}
