package at.ac.tuwien.sepm.groupphase.backend.repository.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepositoryCustom;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
        LoginAttempts loginAttempts = LoginAttempts.builder().setAttempts(0).setBlocked(false).setUser(user).build();
        loginAttempts.setUserSynch(user);
        entityManager.persist(user);
        return user;
    }
}
