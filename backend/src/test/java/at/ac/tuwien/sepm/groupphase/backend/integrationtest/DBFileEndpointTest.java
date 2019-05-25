package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.DBFile;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.DBFileRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

public class DBFileEndpointTest extends BaseIntegrationTest {

    private static final String DBFILE_ENDPOINT = "/files";
    private static final String SPECIFIC_DBFILE_PATH = "/{dbfileId}";

    private static final long TEST_DBFILE_ID = 1L;
    private static final String TEST_DBFILE_NAME = "test.txt";
    private static final String TEST_DBFILE_TYPE = "";

    private InputStream is;
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DBFileRepository dbFileRepository;

    @Test
    public void uploadFileAsAdmin() throws Exception {
        /*
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "multipart/form-data", is);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(mockMultipartFile).contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotNull(result.getResponse().getContentAsString());
        Assert.assertEquals(TEST_DBFILE_ID, Long.parseLong(result.getResponse().getContentAsString()));

         */
/*
        ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("file", resource);
        ResponseEntity<String> response = this.restTemplate.postForEntity("/", map,
            String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString())
            .startsWith("http://localhost:" + this.port + "/");
        then(storageService).should().store(any(MultipartFile.class));

 */
    }

    /*
    @Test
    public void findAllUsersUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllUsersAsUser() {
        BDDMockito.
            given(dbFileRepository.findAll()).
            willReturn(Collections.singletonList(
                User.builder()
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
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
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
            given(dbFileRepository.findById(TEST_USER_ID)).
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
            given(dbFileRepository.findById(TEST_USER_ID)).
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

     */

}
