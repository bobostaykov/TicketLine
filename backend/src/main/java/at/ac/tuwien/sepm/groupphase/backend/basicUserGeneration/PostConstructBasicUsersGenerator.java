package at.ac.tuwien.sepm.groupphase.backend.basicUserGeneration;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostConstructBasicUsersGenerator {
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @EventListener
    public void run(ContextRefreshedEvent contextRefreshedEvent) throws Exception {
        //user service checks if already existing
        LOGGER.info("initializing basic users");
        try{
            LOGGER.info("creating user admin");
            if(userService.findUserByName("admin") == null){
                userService.createUser(UserDTO.builder().type(UserType.ADMIN).password("password").username("admin").userSince(LocalDateTime.now()).build());
            }

        }catch (NotFoundException e){
            LOGGER.info("creating user admin");
            userService.createUser(UserDTO.builder().type(UserType.ADMIN).password("password").username("admin").userSince(LocalDateTime.now()).build());
        }
        try {
            if (userService.findUserByName("user") == null) {
                LOGGER.info("creating user admin");
                userService.createUser(UserDTO.builder().type(UserType.SELLER).password("password").username("user").lastLogin(LocalDateTime.now()).userSince(LocalDateTime.now()).build());
            }
        }catch(NotFoundException e) {
            LOGGER.info("creating user admin");
            userService.createUser(UserDTO.builder().type(UserType.SELLER).password("password").username("user").lastLogin(LocalDateTime.now()).userSince(LocalDateTime.now()).build());
            }
        }
}
