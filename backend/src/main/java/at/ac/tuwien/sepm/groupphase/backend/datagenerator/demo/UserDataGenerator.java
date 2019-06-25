package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Profile("generateData")
@Component
public class UserDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String USER_NAME = "user";
    private static final String ADMIN_NAME = "admin";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";

    Faker faker = new Faker();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void generate(){
        if(userRepository.count() > 0){
            LOGGER.info("Users already generated");
        }else {
            LOGGER.info("Generating users");
            Long id = 1L, cn = 1L, sn = 1L;
            User user = User.builder().username(USER_NAME).password(passwordEncoder.encode(USER_PASSWORD)).type(UserType.SELLER).userSince(LocalDateTime.now()).build();
            User admin = User.builder().username(ADMIN_NAME).password(passwordEncoder.encode(ADMIN_PASSWORD)).type(UserType.ADMIN).userSince(LocalDateTime.now()).build();

            userRepository.createUser(user);
            userRepository.createUser(admin);
        }
    }
}
