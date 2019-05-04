package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.Is.is;

import java.time.LocalDate;

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
}
