package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTestWithMockedUserCredentials;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class UserEndpointTest extends BaseIntegrationTestWithMockedUserCredentials {

    @MockBean
    private UserRepository userRepository;

    private static final String USER_ENDPOINT = "/users";
    private static final String SPECIFIC_USER_PATH = "/{userId}";

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_USER_ID_ERROR = -1L;
    private static final String TEST_USER_NAME = "Messi";
    private static final String TEST_USER_PASS = "Messi123";
    private static final UserType TEST_USER_TYPE = UserType.ADMIN;
    private static final LocalDateTime TEST_USER_SINCE = LocalDateTime.of(1011, 11, 11, 11, 11, 11, 11);
    private static final LocalDateTime TEST_USER_LAST_LOGIN = LocalDateTime.of(2012, 12, 12, 12, 12, 12, 12);
    private static final List<News> TEST_READ_NEWS = new ArrayList<News>();
    private static final List<SimpleNewsDTO> TEST_READ_NEWS_DTO = new ArrayList<>();

    @Test
    public void findAllUsersUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT + "?username=null&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllUsersAsUser() {
        BDDMockito.
            given(userRepository.findAll(PageRequest.of(0, 10))).
            willReturn(
                new PageImpl<>(
                    Collections.singletonList(
                        User.builder()
                            .id(TEST_USER_ID)
                            .username(TEST_USER_NAME)
                            .password(TEST_USER_PASS)
                            .type(TEST_USER_TYPE)
                            .userSince(TEST_USER_SINCE)
                            .lastLogin(TEST_USER_LAST_LOGIN)
                            .readNews(TEST_READ_NEWS)
                            .build()), PageRequest.of(0,10), 1
                )
            );
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT + "?username=null&page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void findAllUsersAsAdmin() {
        BDDMockito.
            given(userRepository.findAll(PageRequest.of(0, 10))).
            willReturn(
                new PageImpl<>(
                    Collections.singletonList(
                    User.builder()
                        .id(TEST_USER_ID)
                        .username(TEST_USER_NAME)
                        .password(TEST_USER_PASS)
                        .type(TEST_USER_TYPE)
                        .userSince(TEST_USER_SINCE)
                        .lastLogin(TEST_USER_LAST_LOGIN)
                        .readNews(TEST_READ_NEWS)
                        .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT + "?username=null&page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        UserDTO readValue = response.jsonPath().getList("content", UserDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), TEST_USER_ID);
        Assert.assertEquals(readValue.getUsername(), TEST_USER_NAME);
        Assert.assertEquals(readValue.getPassword(), TEST_USER_PASS);
        Assert.assertEquals(readValue.getType(), TEST_USER_TYPE);
        Assert.assertEquals(readValue.getLastLogin(), TEST_USER_LAST_LOGIN);
        Assert.assertEquals(readValue.getUserSince(), TEST_USER_SINCE);
        Assert.assertTrue(readValue.getReadNews().isEmpty());
    }

    @Test
    public void findSpecificUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT + SPECIFIC_USER_PATH, TEST_USER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificUserAsUser() {
        BDDMockito.
            given(userRepository.findById(TEST_USER_ID)).
            willReturn(Optional.of(User.builder()
                .id(TEST_USER_ID)
                .username(TEST_USER_NAME)
                .type(TEST_USER_TYPE)
                .userSince(TEST_USER_SINCE)
                .lastLogin(TEST_USER_LAST_LOGIN)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT + SPECIFIC_USER_PATH, TEST_USER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void findSpecificNonExistingUserNotFoundAsAdmin() {
        BDDMockito.
            given(userRepository.findById(TEST_USER_ID)).
            willReturn(Optional.empty());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT + SPECIFIC_USER_PATH, TEST_USER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void createUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(UserDTO.builder()
                .id(TEST_USER_ID)
                .username(TEST_USER_NAME)
                .type(TEST_USER_TYPE)
                .userSince(TEST_USER_SINCE)
                .lastLogin(TEST_USER_LAST_LOGIN)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void createUserUnauthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(UserDTO.builder()
                .id(TEST_USER_ID)
                .username(TEST_USER_NAME)
                .type(TEST_USER_TYPE)
                .userSince(TEST_USER_SINCE)
                .lastLogin(TEST_USER_LAST_LOGIN)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void createUserAsAdmin() {
        BDDMockito.
            given(userRepository.createUser(any(User.class))).
            willReturn(User.builder()
                .id(TEST_USER_ID)
                .username(TEST_USER_NAME)
                .password(TEST_USER_PASS)
                .type(TEST_USER_TYPE)
                .userSince(TEST_USER_SINCE)
                .lastLogin(TEST_USER_LAST_LOGIN)
                .readNews(TEST_READ_NEWS)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(UserDTO.builder()
                .username(TEST_USER_NAME)
                .password(TEST_USER_PASS)
                .type(TEST_USER_TYPE)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CREATED.value()));
        UserDTO dto = response.as(UserDTO.class);
        Assert.assertThat(response.as(UserDTO.class), is(UserDTO.builder()
            .id(TEST_USER_ID)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASS)
            .type(TEST_USER_TYPE)
            .userSince(TEST_USER_SINCE)
            .lastLogin(TEST_USER_LAST_LOGIN)
            .readNews(TEST_READ_NEWS_DTO)
            .build()));
    }

//    @Test//(expected = ResponseStatusException.class)
//    public void createUserWithExistingUsernameAsAdmin() {
//        BDDMockito.
//            given(userRepository.save(User.builder()
//                .id(TEST_USER_ID_ERROR)
//                .build())).
//            willReturn(User.builder()
//                .id(TEST_USER_ID_ERROR)
//                .build());
////            willThrow(DataIntegrityViolationException.class);
//        Response response1 = RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
//            .body(UserDTO.builder()
//                .username(TEST_USER_NAME)
//                .password(TEST_USER_PASS)
//                .type(TEST_USER_TYPE)
//                .build())
//            .when().post(USER_ENDPOINT)
//            .then().extract().response();
//        Response response2 = RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
//            .body(UserDTO.builder()
//                .username(TEST_USER_NAME)
//                .password(TEST_USER_PASS)
//                .type(TEST_USER_TYPE)
//                .build())
//            .when().post(USER_ENDPOINT)
//            .then().extract().response();
//        Assert.assertThat(response1.getStatusCode(), is(HttpStatus.CREATED.value()));
//        Assert.assertThat(response2.getStatusCode(), is(HttpStatus.CREATED.value()));
//        Assert.assertThat(response2.as(UserDTO.class), is(UserDTO.builder()
//            .id(TEST_USER_ID_ERROR)
//            .build()));
//    }

//    @Test
//    public void deleteUserAsUser() {
//        BDDMockito.
//            given(userRepository.createUser(any(User.class))).
//            willReturn(User.builder()
//                .id(TEST_USER_ID)
//                .username(TEST_USER_NAME)
//                .password(TEST_USER_PASS)
//                .type(TEST_USER_TYPE)
//                .userSince(TEST_USER_SINCE)
//                .lastLogin(TEST_USER_LAST_LOGIN)
//                .readNews(TEST_READ_NEWS)
//                .build());
//        BDDMockito.verify(userRepository).deleteById(TEST_USER_ID);
////            given(userRepository.deleteById(TEST_USER_ID));
//        Response response = RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
//            .when().get(USER_ENDPOINT)
//            .then().extract().response();
//        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
//    }
//
//    @Test
//    public void deleteUserAsAdmin() {
//        BDDMockito.verify(userRepository).deleteById(TEST_USER_ID);
////            given(userRepository.findAll()).
////            willReturn(Collections.singletonList(
////                User.builder()
////                    .id(TEST_USER_ID)
////                    .username(TEST_USER_NAME)
////                    .password(TEST_USER_PASS)
////                    .type(TEST_USER_TYPE)
////                    .userSince(TEST_USER_SINCE)
////                    .lastLogin(TEST_USER_LAST_LOGIN)
////                    .readNews(TEST_READ_NEWS)
////                    .build()));
//        Response response = RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
//            .when().delete(USER_ENDPOINT + "/1")
//            .then().extract().response();
//        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
//        Assert.assertNull(response.as(UserDTO.class));
//    }

}
