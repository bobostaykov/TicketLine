package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Profile("generatePerformanceTestData")
@Component
public class UserPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String USER_NAME = "user";
    private static final String ADMIN_NAME = "admin";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";
    private static final String BOTHIFY_PASSWORD_STRING = "########";

    Faker faker = new Faker();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserPerformanceTestDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void generate(){
        if(userRepository.count() > 0){
            LOGGER.info("Users already generated");
        }else {
            LOGGER.info("Generating users");
            for(Long id = 1L; id <= NUM_OF_USERS; id++) {
                userRepository.createUser(User.builder()
                    .id(id)
                    .type(id % 2 == 0 ? UserType.ADMIN : UserType.SELLER).username(faker.name().username().toLowerCase())
                    .password(passwordEncoder.encode(faker.bothify(BOTHIFY_PASSWORD_STRING)))
                    .userSince(LocalDateTime.now())
                    .build());
            }
        }
    }
}
