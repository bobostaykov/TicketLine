package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class NewsEndpointTest extends BaseIntegrationTest {

    private static final String NEWS_ENDPOINT = "/news";
    private static final String LATEST_NEWS_ENDPOINT = "/news/unread";
    private static final String SPECIFIC_NEWS_PATH = "/{newsId}";

    private static final String TEST_NEWS_TEXT = "TestNewsText";
    private static final String TEST_NEWS_TITLE = "title";
    private static final LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final long TEST_NEWS_ID = 1L;

    @MockBean
    private NewsRepository newsRepository;

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
            given(newsRepository.findAllByOrderByPublishedAtDesc()).
            willReturn(Collections.singletonList(
                News.builder()
                    .id(TEST_NEWS_ID)
                    .title(TEST_NEWS_TITLE)
                    .text(TEST_NEWS_TEXT)
                    .publishedAt(TEST_NEWS_PUBLISHED_AT)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO[].class)), is(Collections.singletonList(
            at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .summary(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())));
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
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.class), is(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
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
            .body(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
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
            .body(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
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
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.builder()
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.class), is(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }
}