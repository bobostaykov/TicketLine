package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class LoginAttemptTest {


    @Autowired
    private LoginAttemptsRepository loginAttemptsRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HeaderTokenAuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;



    private final Integer MAX_NUMBER_OF_ATTEMPTS = 5;
    UserDTO testUserDTO1 = UserDTO.builder().type(UserType.SELLER).username("testUser1").password("password").build();
    UserDTO testUserDTO2 = UserDTO.builder().type(UserType.SELLER).username("testUser2").password("password").build();
    LoginAttempts loginAttempts1;
    LoginAttempts loginAttempts2;
    boolean init = false;

    @Before
    public void before() throws ServiceException {
        if(!init) {
            testUserDTO1 = userService.createUser(testUserDTO1);
            testUserDTO2 = userService.createUser(testUserDTO2);
            loginAttempts1 = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO1));
            loginAttempts2 = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO2));
            loginAttempts2.setBlocked(true);
            loginAttempts2.setNumberOfAttempts(MAX_NUMBER_OF_ATTEMPTS + 1);
            loginAttempts2 = loginAttemptsRepository.save(loginAttempts2);
            init = true;
        }

    }
    @Test
    public void blockedUserIsDenied(){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUserDTO2.getUsername(), testUserDTO2.getPassword()));
        Assert.assertFalse(auth.isAuthenticated());
    }
    @Test
    public void failedAttemptIteratesCounter(){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUserDTO1.getUsername(), "wrongPW"));
        LoginAttempts loginAttempts = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO1));
        Assert.assertEquals(1, loginAttempts.getNumberOfAttempts());
    }
    @Test
    public void successfulAttemptResetsCounter(){
        loginAttempts1.setNumberOfAttempts(2);
        loginAttemptsRepository.save(loginAttempts1);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUserDTO1.getUsername(), testUserDTO1.getPassword()));
        loginAttempts1 = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO1));
        Assert.assertEquals(1, loginAttempts1.getNumberOfAttempts());

        //reset
        loginAttempts1.setNumberOfAttempts(0);
        loginAttemptsRepository.save(loginAttempts1);
    }
    @Test
    public void reachingTheAttemptLimitBlocksUser(){
        loginAttempts1.setNumberOfAttempts(MAX_NUMBER_OF_ATTEMPTS);
        loginAttemptsRepository.save(loginAttempts1);
        authenticationManager.authenticate(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(testUserDTO1.getUsername(), "wrongPW")));
        loginAttempts1 = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO1));
        Assert.assertTrue(loginAttempts1.isBlocked());
        //reset
        loginAttempts1.setNumberOfAttempts(0);
        loginAttempts1.setBlocked(false);
        loginAttemptsRepository.save(loginAttempts1);
    }
    @Test
    public void unblickingUserResetsLoginAttempts(){
        //todo figure out with which credentials unlock happoens
        userService.unblockUser(testUserDTO2.getId());
        loginAttempts2 = loginAttemptsRepository.getByUser(userMapper.userDTOToUser(testUserDTO2));
        Assert.assertFalse(loginAttempts2.isBlocked());
        Assert.assertEquals(0 ,loginAttempts2.getNumberOfAttempts());
    }


}
