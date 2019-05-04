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
}
