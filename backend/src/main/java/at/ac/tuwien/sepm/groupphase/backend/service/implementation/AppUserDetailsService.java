package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptsRepository loginAttemptsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findOneByUsername(username);
        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if(optionalUser.isPresent()){
            LOGGER.info("Authenticating user: " + username);
            User user = optionalUser.get();
            if(loginAttemptsRepository.findById(user.getId()).get().isBlocked()){
                LOGGER.info("Authentication denied, user is blocked");
                throw new BadCredentialsException("is blocked");
            }
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            if(user.getType().equals(UserType.ADMIN)){
                builder.authorities("ADMIN", "USER");
            }else{
                builder.authorities("USER");
            }
            LOGGER.info("Authentication Successful");

        }else{
            LOGGER.info("Authentication Denied, user " + username + " does not exist");
            throw new BadCredentialsException("");
        }
        return builder.build();
    }
}
