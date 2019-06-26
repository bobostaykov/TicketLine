package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);

    public LoginAttemptServiceImpl(UserService userService, LoginAttemptsRepository loginAttemptsRepository, UserMapper userMapper, UserRepository userRepository) {
        this.userService = userService; this.loginAttemptsRepository = loginAttemptsRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void failedLogin(String username) throws NotFoundException {

            Optional<User> found = userRepository.findOneByUsername(username);
            if (found.isPresent()){
                if(!found.get().getType().equals(UserType.ADMIN)){
                    User user = found.get();
                    LoginAttempts attempts = loginAttemptsRepository.getByUser(user);
                    LOGGER.info("Failed Login Attempt number: "+ attempts +"by user: "+ user.getUsername());
                    attempts.setNumberOfAttempts(attempts.getNumberOfAttempts() + 1);
                    if(attempts.getNumberOfAttempts() > MAX_NUMBER_OF_ATTEMPTS){
                        attempts.setBlocked(true);
                    }
                    loginAttemptsRepository.save(attempts);
                }

            }else{
                throw new NotFoundException("could not find user with username: " + username);
            }

    }

    @Override
    public void successfulLogin(String username) {
        Optional<User> opt = userRepository.findOneByUsername(username);
        if (opt.isPresent()) {
            UserDTO user = userMapper.userToUserDTO(opt.get());
            if (!user.getType().equals(UserType.ADMIN)){
                LoginAttempts attempts = loginAttemptsRepository.getLoginAttemptsById(user.getId());
                attempts.setNumberOfAttempts(0);
                loginAttemptsRepository.save(attempts);
            }
        }

    }




}
