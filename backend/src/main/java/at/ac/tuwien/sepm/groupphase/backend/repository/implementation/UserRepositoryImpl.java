package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts_;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.User_;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private final EntityManager entityManager;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User createUser(User user) {
        LOGGER.debug("UserRepositoryImpl: createUser");
        LoginAttempts loginAttempts = LoginAttempts.builder().setAttempts(0).setBlocked(false).setUser(user).build();
        loginAttempts.setUserSynch(user);
        entityManager.persist(user);
        return user;
    }

    @Override
    public List<User> findAllBlockedUsers() {
        LOGGER.debug("UserRepositoryImpl: findAllBlockedUsers");
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        //Collection of conditions
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<User> cq = cBuilder.createQuery(User.class);
        Root<User> userRoot = cq.from(entityManager.getMetamodel().entity(User.class));
        Join<User, LoginAttempts> userLoginAttemptsJoin = userRoot.join(User_.LOGIN_ATTEMPTS);
        predicates.add(cBuilder.isTrue(userLoginAttemptsJoin.get(LoginAttempts_.BLOCKED)));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.select(userRoot);
        return entityManager.createQuery(cq).getResultList();
    }
}
