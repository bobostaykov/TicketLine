package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@JdbcTest
@RunWith(SpringRunner.class)
public class LoginAttemtTest {
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private UserService userService;
    @Autowired
    LoginAttemptsRepository loginAttemptsRepository;
    UserDTO userDTO = UserDTO.builder().type(UserType.SELLER).name("testUser").build()
}
