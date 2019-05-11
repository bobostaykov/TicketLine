package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final Integer MAX_NUMBER_OF_ATTEMPTS = 4;
    private final UserService userService;
    private final LoginAttemptsRepository loginAttemptsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);

    public LoginAttemptServiceImpl(UserService userService, LoginAttemptsRepository loginAttemptsRepository) {
        this.userService = userService; this.loginAttemptsRepository = loginAttemptsRepository;
    }

    @Override
    public void failedLogin(String username) {
        Optional<User> found = userService.findOneByName(username);
        if (found.isPresent() && !found.get().getType().equals("ADMIN")){
            Long id = found.get().getId();
            Integer attempts = loginAttemptsRepository.findById(id).get().getNumberOfAttempts();
            LOGGER.info("Failed Login Attempt number: "+ attempts +"by user: "+ found.get().getName());
            if(attempts > MAX_NUMBER_OF_ATTEMPTS){
                loginAttemptsRepository.blockUser(id);
            }else{
                loginAttemptsRepository.setAttemps(++attempts, id);
            }
        }
    }

    @Override
    public void successfulLogin(String username) {
        Optional<User> found = userService.findOneByName(username);
        if (found.isPresent() && !found.get().getType().equals("ADMIN")){
            loginAttemptsRepository.setAttemps(0, found.get().getId());
        }
    }

    @Override
    public void unblockUser(User user) {
        LOGGER.info("user: " + user.getName() + " was unblocked");
        loginAttemptsRepository.unblockUser(user.getId());
    }

    @Override
    public void blockUser(User user) {
        LOGGER.info("user: " + user.getName() + " was blocked");
        loginAttemptsRepository.blockUser(user.getId());
    }

    @Override
    public void initializeLoginAttempts(User user) {
        loginAttemptsRepository.save(new LoginAttempts(user.getId(), user, 0, false));
    }
}
