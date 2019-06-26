package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationToken;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.PasswordChangeRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class UserIntegrationTest extends BaseIntegrationTest {
    private static final String USER_ENDPOINT = "/users";
    private static final String SPECIFIC_USER_PATH = "/{userId}";
    private static final String BLOCKED_USER_PATH = "/blocked";
    private static final String UNBLOCK_USER_PATH = "/unblock/";
    private static final long TEST_USER_ID_1 = 1L;
    private static final String TEST_USER_NAME_1 = "Messi";
    private static final String TEST_USER_PASS_1 = "password";
    private static final UserType TEST_USER_TYPE_1 = UserType.SELLER;
    private static final LocalDateTime TEST_USER_SINCE_1 = LocalDateTime.of(1011, 11, 11, 11, 11, 11, 11);
    private static final LocalDateTime TEST_USER_LAST_LOGIN_1 = LocalDateTime.of(2012, 12, 12, 12, 12, 12, 12);
    private static final List<News> TEST_READ_NEWS_1 = new ArrayList<News>();
    private static final List<SimpleNewsDTO> TEST_READ_NEWS_DTO = new ArrayList<SimpleNewsDTO>();
    private static final long TEST_USER_ID_2 = 1L;
    private static final String TEST_USER_NAME_2 = "Ronaldo";
    private static final String TEST_USER_PASS_2 = "RealMadrid";
    private static final UserType TEST_USER_TYPE_2 = UserType.ADMIN;
    private static final LocalDateTime TEST_USER_SINCE_2 = LocalDateTime.of(1011, 11, 11, 11, 11, 11, 11);
    private static final LocalDateTime TEST_USER_LAST_LOGIN_2 = LocalDateTime.of(2012, 12, 12, 12, 12, 12, 12);
    private static final List<News> TEST_READ_NEWS_2 = new ArrayList<News>();
    private static final List<SimpleNewsDTO> TEST_READ_NEWS_DTO_2 = new ArrayList<SimpleNewsDTO>();
    private static final Long INVALID_ID = 99999L;
    private static final String CHANGED_PASSWORD = "newPassword";
    private User testUser1;
    private User testUser2;
    String validAdminTokenWithPrefix2;
    String validUserTokenWithPrefix2;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptsRepository loginAttemptsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HeaderTokenAuthenticationService authenticationService;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        try {
            if(userRepository.findOneByUsername(TEST_USER_NAME_1).isEmpty()){
                testUser1 = userMapper.userDTOToUser(userService.createUser(UserDTO.builder()
                    .username(TEST_USER_NAME_1)
                    .userSince(TEST_USER_SINCE_1)
                    .lastLogin(TEST_USER_LAST_LOGIN_1)
                    .password(TEST_USER_PASS_1)
                    .type(TEST_USER_TYPE_1)
                    .build()));
            }
            else{
                testUser1 = userRepository.findOneByUsername(TEST_USER_NAME_1).get();
            }
            if(userRepository.findOneByUsername(TEST_USER_NAME_2).isEmpty()){
                testUser2 = userMapper.userDTOToUser(userService.createUser(UserDTO.builder()
                    .username(TEST_USER_NAME_2)
                    .userSince(TEST_USER_SINCE_2)
                    .lastLogin(TEST_USER_LAST_LOGIN_2)
                    .password(TEST_USER_PASS_2)
                    .type(TEST_USER_TYPE_2)
                    .build()));
            }else{
                testUser2 = userRepository.findOneByUsername(TEST_USER_NAME_2).get();
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void AuthenticationWithCorrectCredentials_resultsInNotNullToken(){
        AuthenticationToken token = authenticationService.authenticate(TEST_USER_NAME_1, TEST_USER_PASS_1);
        Assert.assertNotNull(token);
    }

    @Test
    public void AuthenticationWithBadCredentials_throwsBadCredentialsException(){
        thrown.expect(BadCredentialsException.class);
        AuthenticationToken token = authenticationService.authenticate(TEST_USER_NAME_1, TEST_USER_PASS_2);
    }

    @Test
    public void AuthenticationWithCorrectCredentialsAndBlockedUser_resultsInNoAuthentication() throws ServiceException {
        thrown.expect(InternalAuthenticationServiceException.class);
        userService.unblockUser(testUser1.getId());
        userService.blockUser(testUser1.getId());
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertThat(attempts.isBlocked(), is(true));
        authenticationService.authenticate(TEST_USER_NAME_1, TEST_USER_PASS_1);
        //reset
        userService.unblockUser(testUser1.getId());
        Assert.assertThat(attempts.isBlocked(), is(false));
    }

    @Test
    public void blockUserAsAdmin_resultsInUserBeingBlocked(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + "/" + testUser1.getId());
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertThat(attempts.isBlocked(), is(true));
        userService.unblockUser(testUser1.getId());
        //reset
        attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertThat(attempts.isBlocked(), is(false));
    }
    @Test
    public void changePasswordAsAdmin_resultsInUserBeingBlocked() throws ServiceException {
        PasswordChangeRequest passwordChangeRequest = PasswordChangeRequest.builder().password(CHANGED_PASSWORD).userId(testUser1.getId()).userName(testUser1.getUsername()).build();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(passwordChangeRequest)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(USER_ENDPOINT + "/password");
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        User user  = userRepository.findById(testUser2.getId()).get();
        Assert.assertTrue(user.getPassword() != testUser1.getPassword());
        //reset
        passwordChangeRequest = PasswordChangeRequest.builder().password(TEST_USER_PASS_1).userId(testUser1.getId()).userName(testUser1.getUsername()).build();
        userService.changePassword(passwordChangeRequest);
    }

    @Test
    public void blockUserAsUser_resultsInNoAuthorization(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + SPECIFIC_USER_PATH, testUser1.getId())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));

    }

    @Test
    public void unblockUserAsUser_resultsInNoAuthorization() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + UNBLOCK_USER_PATH + testUser1.getId())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getBlockedUsersAsUser_resultsInNoAuthorization() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT + BLOCKED_USER_PATH + "?username=null")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void unblockUserAsAdmin_resultsInUserBeingUnblocked() throws ServiceException {
        userService.unblockUser(testUser1.getId());
        userService.blockUser(testUser1.getId());
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertThat(attempts.isBlocked(), is(true));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + UNBLOCK_USER_PATH + testUser1.getId())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertThat(attempts.isBlocked(), is(false));

    }

    @Test
    public void authenticationOfUserWithBadCredentials_raisesAttemptsCounter(){
        userService.unblockUser(testUser1.getId());
        try{
            authenticationService.authenticate(TEST_USER_NAME_1, TEST_USER_PASS_2);
        }catch (BadCredentialsException e){

        }
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertTrue(attempts.getNumberOfAttempts() > 0);
    }

    @Test
    public void authenticationOfUserWithGoodCredentials_setsCounterBackToZero(){
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        attempts.setNumberOfAttempts(1);
        loginAttemptsRepository.save(attempts);
        authenticationService.authenticate(TEST_USER_NAME_1, TEST_USER_PASS_1);
        attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertEquals(0, attempts.getNumberOfAttempts() );
    }

    @Test
    public void authenticationOfAdminWithBadCredentials_doesNotChangeCounter(){
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser2.getId()).get();
        Assert.assertEquals(0, attempts.getNumberOfAttempts());
        try{
            authenticationService.authenticate(TEST_USER_NAME_2, TEST_USER_PASS_1);
        }catch (BadCredentialsException e){

        }
        attempts = loginAttemptsRepository.findById(testUser2.getId()).get();
        Assert.assertEquals(0, attempts.getNumberOfAttempts());
    }

    @Test
    public void unblockingUserAsAdminWithInvalidId_returnsNotFound(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + UNBLOCK_USER_PATH + INVALID_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void blockingAdminAsAdmin_returnsBadRequest(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(USER_ENDPOINT + BLOCKED_USER_PATH + SPECIFIC_USER_PATH, testUser2.getId())
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void gettingAllBlockedUsersAsAdmin_returnsPageWithBlockedUsers() throws ServiceException {
        userService.blockUser(testUser1.getId());
        LoginAttempts attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertTrue(attempts.isBlocked());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .headers(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT + BLOCKED_USER_PATH + "?username=null&page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        UserDTO readValue = response.jsonPath().getList("content", UserDTO.class).get(0);
        UserDTO expectedUser = userMapper.userToUserDTO(userRepository.findById(testUser1.getId()).get());

        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), expectedUser.getId());
        Assert.assertEquals(readValue.getUsername(), expectedUser.getUsername());
        Assert.assertEquals(readValue.getPassword(), expectedUser.getPassword());
        Assert.assertEquals(readValue.getType(), expectedUser.getType());
        Assert.assertEquals(readValue.getLastLogin(), expectedUser.getLastLogin());
        Assert.assertEquals(readValue.getUserSince(), expectedUser.getUserSince());
        Assert.assertTrue(readValue.getReadNews().isEmpty());

        //reset
        userService.unblockUser(testUser1.getId());
        attempts = loginAttemptsRepository.findById(testUser1.getId()).get();
        Assert.assertFalse(attempts.isBlocked());
    }
}
