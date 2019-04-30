package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addLatestNewsFetchDate(String userName, LocalDateTime timestamp) {
        try {
            //ToDo: optional improvement: make sure every user exists => no chedk for existence of user is needed
            if (userRepository.findOneByUserName(userName).isEmpty()) {
                User user = new User();
                user.setUserName(userName);
                user.setLastFetchTimestamp(timestamp);
                userRepository.save(user);
            }
            else {
                userRepository.updateLastNewsFetchTimestampByUserName(userName, timestamp);
            }
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
