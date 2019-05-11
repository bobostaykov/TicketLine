package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;

public class ShowEndpointTest extends BaseIntegrationTest {

    private static final String SHOWS_ENDPOINT = "/shows";
    private static final String SHOWS_EVENT_NAME = "ent1";

    @MockBean
    private ShowRepository showRepository;

    @Test
    public void findAllShowsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SHOWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    /*
    @Test
    public void findAllShowsAsUserFilteredByEventName(){
        BDDMockito.
            given(showRepository.findAllShowsFilteredByEventName(SHOWS_EVENT_NAME)).
            willReturn(Collections.singletonList(
                Show.builder()
                    .id(CUSTOMER_ID)
                    .name(CUSTOMER_NAME)
                    .firstname(CUSTOMER_FIRSTNAME)
                    .email(CUSTOMER_EMAIL)
                    .birthday(CUSTOMER_BIRTHDAY)
                    .build()));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_EVENT_NAME)
            .then().extract().response();

        Assert.assertThat(Arrays.asList(response.as(ShowDTO[].class)), is(Collections.singletonList(
            ShowDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }
    */
}
