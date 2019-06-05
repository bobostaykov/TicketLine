package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class TicketEndpointTest extends BaseIntegrationTest {

    private static final String TICKET_ENDPOINT = "/tickets";
    private static final String RESERVATED_TICKET = "/reservated";
    private static final String FIND_BY_NAME = "/name";
    private static final String BUY_TICKET = "/buy";
    private static final String SPECIFIC_TICKET_PATH = "/{id}";

    private static final long TEST_TICKET_ID = 1L;
    private static final TicketStatus TEST_TICKET_STATUS = TicketStatus.RESERVATED;

    private static final long TEST_SHOW_ID = 1L;

    private static final String TEST_CUSTOMER_NAME = "TestName";
    private static final String TEST_CUSTOMER_FIRSTNAME = "TestFirstName";
    private static final String TEST_CUSTOMER_EMAIL = "testCustomer@test.com";
    private static final LocalDate TEST_CUSTOMER_BIRTHDATE = LocalDate.of(1996, 11, 13);
    private static final long TEST_CUSTOMER_ID = 1L;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Before
    public void init() {
        RestAssured.defaultParser = Parser.JSON;
    }

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

    @Test
    public void buySpecificReservatedTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void buySpecificReservatedTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TicketStatus.SOLD)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .status(TicketStatus.SOLD)
            .build()));
    }

    @Test
    public void buySpecificReservatedTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TicketStatus.SOLD)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .status(TicketStatus.SOLD)
            .build()));
    }

    @Test
    public void findReservatedTicketsByCustomerUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(ShowDTO.builder()
                .id(TEST_SHOW_ID)
                .build())
            .param("customerName", TEST_CUSTOMER_NAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findReservatedTicketsByCustomerNameAndShowAsUser() {
        BDDMockito.
            given(ticketRepository.findAllByCustomerAndShowWithStatusReservated(any(Customer.class), any(Show.class))).
            willReturn(Collections.singletonList(
                Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(customerRepository.findAllByName(TEST_CUSTOMER_NAME)).
            willReturn(Collections.singletonList(
                Customer.builder()
                .id(TEST_CUSTOMER_ID)
                .name(TEST_CUSTOMER_NAME)
                .firstname(TEST_CUSTOMER_FIRSTNAME)
                .email(TEST_CUSTOMER_EMAIL)
                .birthday(TEST_CUSTOMER_BIRTHDATE)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ShowDTO.builder()
            .id(TEST_SHOW_ID)
            .build())
            .param("customerName", TEST_CUSTOMER_NAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build())));
    }

    @Test
    public void findReservatedTicketsByCustomerNameAndShowAsAdmin() {
        BDDMockito.
            given(ticketRepository.findAllByCustomerAndShowWithStatusReservated(any(Customer.class), any(Show.class))).
            willReturn(Collections.singletonList(
                Ticket.builder()
                    .id(TEST_TICKET_ID)
                    .status(TEST_TICKET_STATUS)
                    .build()));
        BDDMockito.
            given(customerRepository.findAllByName(TEST_CUSTOMER_NAME)).
            willReturn(Collections.singletonList(
                Customer.builder()
                    .id(TEST_CUSTOMER_ID)
                    .name(TEST_CUSTOMER_NAME)
                    .firstname(TEST_CUSTOMER_FIRSTNAME)
                    .email(TEST_CUSTOMER_EMAIL)
                    .birthday(TEST_CUSTOMER_BIRTHDATE)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(ShowDTO.builder()
                .id(TEST_SHOW_ID)
                .build())
            .param("customerName", TEST_CUSTOMER_NAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build())));
    }

}