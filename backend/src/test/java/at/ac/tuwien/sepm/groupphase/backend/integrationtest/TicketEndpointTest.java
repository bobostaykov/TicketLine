package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class TicketEndpointTest extends BaseIntegrationTest {

    private static final String TICKET_ENDPOINT = "/tickets";
    private static final String RESERVATED_TICKET = "/reservated";
    private static final String SPECIFIC_TICKET_PATH = "/{id}";

    private static final long TEST_TICKET_ID = 1L;
    private static final TicketStatus TEST_TICKET_STATUS = TicketStatus.RESERVATED;

    @MockBean
    private TicketRepository ticketRepository;

    @Test
    public void deleteTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void deleteSpecificTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void deleteSpecificTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void findSpecificTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void findSpecificTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void findSpecificReservatedTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_ENDPOINT + RESERVATED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificReservatedTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + RESERVATED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .status(TEST_TICKET_STATUS)
            .build()));
    }

    @Test
    public void findSpecificReservatedTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + RESERVATED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .status(TEST_TICKET_STATUS)
            .build()));
    }

}
