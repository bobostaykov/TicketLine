package at.ac.tuwien.sepm.groupphase.backend.integrationtest.unit;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoginTests {
    @Autowired
    private LoginAttemptsRepository loginAttemptsRepository;




    private final Integer MAX_NUMBER_OF_ATTEMPTS = 5;
    UserDTO testUserDTO1 = UserDTO.builder().type(UserType.SELLER).username("testUser1").password("password").userSince(LocalDateTime.now()).lastLogin(LocalDateTime.now()).build();
    UserDTO testUserDTO2 = UserDTO.builder().type(UserType.SELLER).username("testUser2").password("password").userSince(LocalDateTime.now()).lastLogin(LocalDateTime.now()).build();
    LoginAttempts loginAttempts1;
    LoginAttempts loginAttempts2;
    boolean init = false;
    @Test
    public void test(){

    }
}