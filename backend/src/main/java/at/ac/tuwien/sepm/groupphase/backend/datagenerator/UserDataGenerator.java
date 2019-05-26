package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    public UserDataGenerator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void generate(){
        if(userRepository.count() > 0){
            LOGGER.info("Users already generated");
        }else {
            LOGGER.info("Generating users");
            Long id = 1L, cn = 1L, sn = 1L;
            User user = User.builder().username(USER_NAME).password(USER_PASSWORD).type(UserType.SELLER).userSince(LocalDateTime.now()).build();
            User admin = User.builder().username(ADMIN_NAME).password(ADMIN_PASSWORD).type(UserType.ADMIN).userSince(LocalDateTime.now()).build();


            userRepository.saveAll(Arrays.asList(user, admin));
        }
    }
}
