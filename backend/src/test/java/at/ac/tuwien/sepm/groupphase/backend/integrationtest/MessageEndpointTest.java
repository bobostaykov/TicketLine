package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.DetailedMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.SimpleMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
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

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class MessageEndpointTest extends BaseIntegrationTest {

    private static final String NEWS_ENDPOINT = "/messages";
    private static final String SPECIFIC_NEWS_PATH = "/{messageId}";

    private static final String TEST_NEWS_TEXT = "TestMessageText";
    private static final String TEST_NEWS_TITLE = "title";
    private static final LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final long TEST_NEWS_ID = 1L;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    public void findAllMessageUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void findAllMessageAsUser() {
        BDDMockito.
            given(messageRepository.findAllByOrderByPublishedAtDesc()).
            willReturn(Collections.singletonList(
                Message.builder()
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
        Assert.assertThat(Arrays.asList(response.as(SimpleMessageDTO[].class)), is(Collections.singletonList(
            SimpleMessageDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .summary(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())));
    }

    @Test
    public void findSpecificMessageUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificMessageAsUser() {
        BDDMockito.
            given(messageRepository.findOneById(TEST_NEWS_ID)).
            willReturn(Optional.of(Message.builder()
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
        Assert.assertThat(response.as(DetailedMessageDTO.class), is(DetailedMessageDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }

    @Test
    public void findSpecificNonExistingMessageNotFoundAsUser() {
        BDDMockito.
            given(messageRepository.findOneById(TEST_NEWS_ID)).
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
    public void publishMessageUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(DetailedMessageDTO.builder()
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
    public void publishMessageUnauthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedMessageDTO.builder()
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
    public void publishMessageAsAdmin() {
        BDDMockito.
            given(messageRepository.save(any(Message.class))).
            willReturn(Message.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedMessageDTO.builder()
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedMessageDTO.class), is(DetailedMessageDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }
}
