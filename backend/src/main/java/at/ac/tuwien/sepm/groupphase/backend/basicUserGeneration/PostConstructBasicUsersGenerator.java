package at.ac.tuwien.sepm.groupphase.backend.basicUserGeneration;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PostConstructBasicUsersGenerator {
    @Autowired
    private UserService userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
/*
    @PostConstruct
    public void run() throws Exception {
        //user service checks if already existing
        LOGGER.info("initializing basic users");
        try{
            if(userRepository.findUserByName("admin") == null){

            }
        }catch (NotFoundException e){
            LOGGER.info("creating user admin");
            userRepository.createUser(UserDTO.builder().type(UserType.ADMIN).password(passwordEncoder.encode("password")).username("admin").userSince(LocalDateTime.now()).build());
        }
        try {
            if (userRepository.findUserByName("user") == null) {
            }
        }catch(NotFoundException e) {
            LOGGER.info("creating user user");
            userRepository.createUser(UserDTO.builder().type(UserType.SELLER).password(passwordEncoder.encode("password")).username("user").lastLogin(LocalDateTime.now()).userSince(LocalDateTime.now()).build());
            }
        }

 */
}
