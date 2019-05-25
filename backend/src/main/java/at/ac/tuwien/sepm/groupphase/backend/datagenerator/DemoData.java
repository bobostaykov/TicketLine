package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DemoData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptsRepository loginAttemptsRepository;
    @Autowired
    private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void run() throws Exception {
        LOGGER.info("Admin created");
        //userService.createUser(UserDTO.builder().type(UserType.ADMIN).password("password").username("admin").build());


        //userService.createUser(UserDTO.builder().type(UserType.SELLER).id(1L).password("password").username("user").lastLogin(LocalDateTime.now()).userSince(LocalDateTime.now()).build());
        LOGGER.info("User created");

    }
}
