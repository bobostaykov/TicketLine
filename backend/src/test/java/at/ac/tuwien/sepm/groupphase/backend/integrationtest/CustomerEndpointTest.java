package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.core.Is.is;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

public class CustomerEndpointTest extends BaseIntegrationTest {
    private static final String CUSTOMER_ENDPOINT = "/customers";
    private static final String CUSTOMER_FILTERED_ID = "/customers?id=1";
    private static final String CUSTOMER_FILTERED_NAME = "/customers?name=ller";
    private static final String CUSTOMER_FILTERED_FIRSTNAME = "/customers?firstname=etr";
    private static final String CUSTOMER_FILTERED_EMAIL = "/customers?email=ller@gmail.co";
    private static final String CUSTOMER_FILTERED_BIRTHDAY = "/customers?birthday=22.07.1982";
    private static final String SPECIFIC_CUSTOMER_PATH = "/{customerID}";

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "Müller";
    private static final String CUSTOMER_FIRSTNAME = "Petra";
    private static final String CUSTOMER_EMAIL = "petra.mueller@gmail.com";
    private static final LocalDate CUSTOMER_BIRTHDAY =
        LocalDate.of(1982,07,22);


    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void findAllMessageUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllCustomersAsUser() {
        BDDMockito.
            given(customerRepository.findAllByOrderByIdAsc()).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedId() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(CUSTOMER_ID, null, null, null, null)).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_FILTERED_ID)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedName() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, "ller", null, null, null)).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_FILTERED_NAME)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedFirstname() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, "etr", null, null)).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_FILTERED_FIRSTNAME)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedEmail() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, null, "ller@gmail.co", null)).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_FILTERED_EMAIL)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedBirthday() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, null, null, LocalDate.of(1982,07,22))).
            willReturn(Collections.singletonList(
                Customer.builder()
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
            .when().get(CUSTOMER_FILTERED_BIRTHDAY)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .firstname(CUSTOMER_FIRSTNAME)
                .email(CUSTOMER_EMAIL)
                .birthday(CUSTOMER_BIRTHDAY)
                .build())));
    }
}
