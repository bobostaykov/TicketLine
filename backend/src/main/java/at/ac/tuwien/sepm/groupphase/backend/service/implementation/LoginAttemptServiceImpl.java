package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final Integer MAX_NUMBER_OF_ATTEMPTS = 4;
    private final UserService userService;
    private final LoginAttemptsRepository loginAttemptsRepository;
    private final UserMapper userMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);

    public LoginAttemptServiceImpl(UserService userService, LoginAttemptsRepository loginAttemptsRepository, UserMapper userMapper) {
        this.userService = userService; this.loginAttemptsRepository = loginAttemptsRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void failedLogin(String username) {

            UserDTO found = userService.findOneByName(username);
            if (found != null && !found.getType().equals("ADMIN")){
                Long id = found.getId();
                Integer attempts = loginAttemptsRepository.findById(id).get().getNumberOfAttempts();
                LOGGER.info("Failed Login Attempt number: "+ attempts +"by user: "+ found.getName());
                if(attempts > MAX_NUMBER_OF_ATTEMPTS){
                    loginAttemptsRepository.blockUser(id);
                }else{
                    loginAttemptsRepository.setAttemps(++attempts, id);
                }
            }

    }

    @Override
    public void successfulLogin(String username) {
        UserDTO found = userService.findOneByName(username);
        if (found != null && !found.getType().equals("ADMIN")){
            loginAttemptsRepository.setAttemps(0, found.getId());
        }
    }

    @Override
    public void unblockUser(User user) {
        LOGGER.info("user: " + user.getName() +"with id: "+ user.getId() + " was unblocked");
        loginAttemptsRepository.unblockUser(user.getId());
    }

    @Override
    public void unblockUserById(Long id) {
        loginAttemptsRepository.unblockUser(id);
        LOGGER.info("user with id: "+ id + "was unblocked");
    }

    @Override
    public void blockUser(User user) {
        LOGGER.info("user: " + user.getName() + " was blocked");
        loginAttemptsRepository.blockUser(user.getId());
    }

    @Override
    public void initializeLoginAttempts(UserDTO user) {
        LOGGER.info("InitializingLoginAttempts for user: "+ user.getName());
        loginAttemptsRepository.save(new LoginAttempts(user.getId(), userMapper.userDTOToUser(user), 0, false));
    }
}
