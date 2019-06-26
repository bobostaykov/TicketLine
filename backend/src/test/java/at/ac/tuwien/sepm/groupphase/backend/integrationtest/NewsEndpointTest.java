package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTestWithMockedUserCredentials;
import at.ac.tuwien.sepm.groupphase.backend.repository.DBFileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.util.IOUtils;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class NewsEndpointTest extends BaseIntegrationTestWithMockedUserCredentials {

    private static final String NEWS_ENDPOINT = "/news";
    private static final String FILE_ENDPOINT = NEWS_ENDPOINT + "/file";
    private static final String UNREAD_NEWS_ENDPOINT = NEWS_ENDPOINT + "/unread";
    private static final String SPECIFIC_NEWS_PATH = "/{newsId}";

    private static final String TEST_NEWS_TEXT = "TestNewsText";
    private static final String TEST_NEWS_TITLE = "title";
    private static final String TEST_NEWS_IMAGEID = "1";
    private static final LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final Long TEST_NEWS_ID = 1L;
    private static final Long TEST_NEWS_ID_2 = 2L;
    private static final String TEST_NEWS_TEXT_2 = "TestNewsText";
    private static final String TEST_NEWS_TITLE_2 = "title";
    private static final String TEST_NEWS_IMAGEID_2 = "1";
    private static final LocalDateTime TEST_NEWS_PUBLISHED_AT_2 =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);


    private static final String TEST_FILE_NAME = "TestFileName";
    private static final String TEST_FILE_TYPE = "jpg";
    private static final Long TEST_FILE_ID = 1L;
    private static final String ADMIN_USERNAME = "admin";
    private static final Long ADMIN_ID = 1L;
    private static final LocalDateTime ADMIN_USER_SINCE = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final LocalDateTime ADMIN_LAST_LOGIN = LocalDateTime.now();

    @MockBean
    private NewsRepository newsRepository;
    @MockBean
    private DBFileRepository dbFileRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void findAllNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void findAllNewsAsUser() {
        BDDMockito.
            given(newsRepository.findAllByOrderByPublishedAtDesc(PageRequest.of(0, 12)))
            .willReturn(
            new PageImpl<>(
                Collections.singletonList(
                    News.builder()
                        .id(TEST_NEWS_ID)
                        .title(TEST_NEWS_TITLE)
                        .text(TEST_NEWS_TEXT)
                        .image(TEST_NEWS_IMAGEID)
                        .publishedAt(TEST_NEWS_PUBLISHED_AT)
                        .build()), PageRequest.of(0,12), 1
            )
        );
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + "?page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        SimpleNewsDTO readValue = response.jsonPath().getList("content", SimpleNewsDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), TEST_NEWS_ID);
        Assert.assertEquals(readValue.getTitle(), TEST_NEWS_TITLE);
        Assert.assertEquals(readValue.getSummary(), TEST_NEWS_TEXT);
        Assert.assertEquals(readValue.getPublishedAt(), TEST_NEWS_PUBLISHED_AT);
    }


    @Test
    public void findSpecificNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificNewsAsUser() {
        BDDMockito.
            given(newsRepository.findOneById(TEST_NEWS_ID)).
            willReturn(Optional.of(News.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedNewsDTO.class), is(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build()));
    }

    @Test
    public void findUnreadNewsAsAdminReturnsOneUnreadNewsEntry() {
        List<News> newsInRepositoryList = new ArrayList<>();
        List<News> newsUnreadList = new ArrayList<>();
        List<News> newsReadList = new ArrayList<>();
        News newsRead = News.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .image(TEST_NEWS_IMAGEID)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build();
        News newsUnread = News.builder()
            .id(TEST_NEWS_ID_2)
            .title(TEST_NEWS_TITLE_2)
            .text(TEST_NEWS_TEXT_2)
            .image(TEST_NEWS_IMAGEID_2)
            .publishedAt(TEST_NEWS_PUBLISHED_AT_2)
            .build();
        newsInRepositoryList.add(newsRead);
        newsInRepositoryList.add(newsUnread);
        newsUnreadList.add(newsUnread);
        newsReadList.add(newsRead);

        BDDMockito.
            given(userRepository.findOneByUsername(ADMIN_USERNAME)).
            willReturn(Optional.of(User.builder()
                .id(ADMIN_ID)
                .username(ADMIN_USERNAME)
                .userSince(ADMIN_USER_SINCE)
                .lastLogin(ADMIN_LAST_LOGIN)
                .type(UserType.ADMIN)
                .readNews(newsReadList)
                .build()));
        BDDMockito.
            given(newsRepository.findAllByOrderByPublishedAtDesc(Pageable.unpaged()))
                .willReturn(new PageImpl<>(newsInRepositoryList, Pageable.unpaged(), 2));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(UNREAD_NEWS_ENDPOINT + "?page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        SimpleNewsDTO readValue = response.jsonPath().getList("content", SimpleNewsDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), TEST_NEWS_ID_2);
        Assert.assertEquals(readValue.getTitle(), TEST_NEWS_TITLE_2);
        Assert.assertEquals(readValue.getSummary(), TEST_NEWS_TEXT_2);
        Assert.assertEquals(readValue.getPublishedAt(), TEST_NEWS_PUBLISHED_AT_2);
    }

    @Test
    public void findSpecificNonExistingNewsNotFoundAsUser() {
        BDDMockito.
            given(newsRepository.findOneById(TEST_NEWS_ID)).
            willReturn(Optional.empty());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void publishNewsAsAdmin() {
        BDDMockito.
            given(newsRepository.save(any(News.class))).
            willReturn(News.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .image(TEST_NEWS_IMAGEID)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedNewsDTO.class), is(DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .image(TEST_NEWS_IMAGEID)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }

    @Test
    public void findImageFileAsAdmin() throws IOException {

        java.io.File file = ResourceUtils.getFile("classpath:data/1.jpg");

        BDDMockito.
            given(dbFileRepository.findOneById(TEST_FILE_ID)).
            willReturn(Optional.of(File.builder()
                .id(TEST_FILE_ID)
                .fileName(TEST_FILE_NAME)
                .fileType(TEST_FILE_TYPE)
                .data(IOUtils.toByteArray(new FileInputStream(file)))
                .build()));

        RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .contentType(ContentType.ANY)
            .when()
            .get(FILE_ENDPOINT + "/" + TEST_FILE_ID)
            .then()
            .assertThat()
            .statusCode(is(HttpStatus.OK.value()))
            .assertThat()
            .contentType(is("image/jpg"));
    }

    @Test
    public void findNotExistingImageFileNotFound() throws IOException {
        BDDMockito.
            given(dbFileRepository.findOneById(TEST_FILE_ID)).
            willReturn(Optional.empty());

        RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .contentType(ContentType.ANY)
            .when()
            .get(FILE_ENDPOINT + "/" + TEST_FILE_ID)
            .then()
            .assertThat()
            .statusCode(is(HttpStatus.NOT_FOUND.value()));
    }


    @Test
    public void postImageFileUnauthorizedAsUser() throws IOException {

        BDDMockito.
            given(dbFileRepository.save(any(File.class))).
            willReturn(File.builder()
                .id(TEST_FILE_ID)
                .fileName(TEST_FILE_NAME)
                .fileType(TEST_FILE_TYPE)
                .build());

        java.io.File file = ResourceUtils.getFile("classpath:data/1.jpg");

        RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("file", file)
            .when()
            .post(FILE_ENDPOINT)
            .then()
            .assertThat()
            .statusCode(is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void postIllegalFileTypeAsAdmin() throws IOException {
        BDDMockito.
            given(dbFileRepository.save(any(File.class))).
            willReturn(File.builder()
                .id(TEST_FILE_ID)
                .fileName(TEST_FILE_NAME)
                .fileType(TEST_FILE_TYPE)
                .build());

        java.io.File file = ResourceUtils.getFile("classpath:data/1.txt");

        RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("file", file)
            .when()
            .post(FILE_ENDPOINT)
            .then()
            .assertThat()
            .statusCode(is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void postImageFileAsAdmin() throws IOException {
        BDDMockito.
            given(dbFileRepository.save(any(File.class))).
            willReturn(File.builder()
                .id(TEST_FILE_ID)
                .fileName(TEST_FILE_NAME)
                .fileType(TEST_FILE_TYPE)
                .build());

        java.io.File file = ResourceUtils.getFile("classpath:data/1.jpg");

        RestAssured
            .given()
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart("file", file)
            .when()
            .post(FILE_ENDPOINT)
            .then()
            .assertThat()
            .body(is(Long.toString(TEST_FILE_ID)))
            .assertThat()
            .statusCode(is(HttpStatus.OK.value()));
    }
}
