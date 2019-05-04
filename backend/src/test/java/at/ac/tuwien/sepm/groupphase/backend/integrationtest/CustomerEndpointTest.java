package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.DetailedMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.SimpleMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class CustomerEndpointTest extends BaseIntegrationTest {

    private static final String CUSTOMER_ENDPOINT = "/customers";

    private static final String TEST_CUSTOMER_NAME = "TestName";
    private static final String TEST_CUSTOMER_FIRSTNAME = "TestFirstName";
    private static final String TEST_CUSTOMER_EMAIL = "testCustomer@test.com";
    private static final LocalDate TEST_CUSTOMER_BIRTHDATE = LocalDate.of(1996, 11, 13);
    private static final long TEST_CUSTOMER_ID = 1L;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void addUserThenHTTPResponseOKAndCustomerIsReturned() {
        BDDMockito.
            given(customerRepository.save(any(Customer.class))).
            willReturn(Customer.builder()
                .id(TEST_CUSTOMER_ID)
                .name(TEST_CUSTOMER_NAME)
                .firstname(TEST_CUSTOMER_FIRSTNAME)
                .email(TEST_CUSTOMER_EMAIL)
                .birthday(TEST_CUSTOMER_BIRTHDATE)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(CustomerDTO.builder()
                .name(TEST_CUSTOMER_NAME)
                .firstname(TEST_CUSTOMER_FIRSTNAME)
                .email(TEST_CUSTOMER_EMAIL)
                .birthday(TEST_CUSTOMER_BIRTHDATE)
                .build())
            .when().post(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
            .id(TEST_CUSTOMER_ID)
            .name(TEST_CUSTOMER_NAME)
            .firstname(TEST_CUSTOMER_FIRSTNAME)
            .email(TEST_CUSTOMER_EMAIL)
            .birthday(TEST_CUSTOMER_BIRTHDATE)
            .build()));
    }

    @Test
    public void addIllegalUserThenHTPPResponseBADREQUEST() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(CustomerDTO.builder()
                .name(TEST_CUSTOMER_NAME)
                .firstname(TEST_CUSTOMER_FIRSTNAME)
                .email(TEST_CUSTOMER_EMAIL)
                .birthday(null)
                .build())
            .when().post(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }
}
