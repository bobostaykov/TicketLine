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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
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

    private static final String CUSTOMER_FILTERED_ID = "/customers?id=1";
    private static final String CUSTOMER_FILTERED_NAME = "/customers?name=ller";
    private static final String CUSTOMER_FILTERED_FIRSTNAME = "/customers?firstname=etr";
    private static final String CUSTOMER_FILTERED_EMAIL = "/customers?email=ller@gmail.co";
    private static final String CUSTOMER_FILTERED_BIRTHDAY = "/customers?birthday=22.07.1982";
    private static final String CUSTOMER_FILTERED_BIRTHDAY_AND_NAME = "/customers?birthday=22.07.1982&name=Müller";
    private static final String CUSTOMER_FILTERED_FIRSTNAME_AND_NAME = "/customers?firstname=Pe&name=Müller";
    private static final String CUSTOMER_FILTERED_FIRSTNAME_AND_NAME_AND_EMAIL = "/customers?firstname=Pe&name=Müller&email=@gmail.com";
    private static final String SPECIFIC_CUSTOMER_PATH = "/{customerID}";

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "Müller";
    private static final String CUSTOMER_FIRSTNAME = "Petra";
    private static final String CUSTOMER_EMAIL = "petra.mueller@gmail.com";
    private static final LocalDate CUSTOMER_BIRTHDAY =
        LocalDate.of(1982,7,22);


    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void findAllNewsUnauthorizedAsAnonymous() {
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
            given(customerRepository.findAllByOrderByIdAsc(PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Customer.builder()
                        .id(CUSTOMER_ID)
                        .name(CUSTOMER_NAME)
                        .firstname(CUSTOMER_FIRSTNAME)
                        .email(CUSTOMER_EMAIL)
                        .birthday(CUSTOMER_BIRTHDAY)
                        .build()), PageRequest.of(0,10), 1
            )
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + "?page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedId() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(CUSTOMER_ID, null, null, null, null, PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_ID + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedName() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, "ller", null, null, null, PageRequest.of(0, 10)))
             .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Customer.builder()
                        .id(CUSTOMER_ID)
                        .name(CUSTOMER_NAME)
                        .firstname(CUSTOMER_FIRSTNAME)
                        .email(CUSTOMER_EMAIL)
                        .birthday(CUSTOMER_BIRTHDAY)
                        .build()), PageRequest.of(0,10), 1
            )
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_NAME + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedFirstname() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, "etr", null, null, PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_FIRSTNAME + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedEmail() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, null, "ller@gmail.co", null, PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_EMAIL + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedBirthday() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, null, null, null, LocalDate.of(1982,7,22), PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_BIRTHDAY + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }


    public void findAllCustomersAsUserFilteredAfterSpecifiedBirthdayAndName() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, "Müller", null, null, LocalDate.of(1982,7,22), PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_BIRTHDAY_AND_NAME + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedFirstnameAndName() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, "Müller", "Pe", null, null, PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_FIRSTNAME_AND_NAME + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findAllCustomersAsUserFilteredAfterSpecifiedFirstnameAndNameAndEmail() {
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, "Müller", "Pe", "@gmail.com", null, PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                    Collections.singletonList(
                        Customer.builder()
                            .id(CUSTOMER_ID)
                            .name(CUSTOMER_NAME)
                            .firstname(CUSTOMER_FIRSTNAME)
                            .email(CUSTOMER_EMAIL)
                            .birthday(CUSTOMER_BIRTHDAY)
                            .build()), PageRequest.of(0,10), 1
                )
            );
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_FILTERED_FIRSTNAME_AND_NAME_AND_EMAIL + "&page=0")
            .then().extract().response();

        CustomerDTO readValue = response.jsonPath().getList("content", CustomerDTO.class).get(0);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getName(), CUSTOMER_NAME);
        Assert.assertEquals(readValue.getFirstname(), CUSTOMER_FIRSTNAME);
        Assert.assertEquals(readValue.getEmail(), CUSTOMER_EMAIL);
        Assert.assertEquals(readValue.getId(), CUSTOMER_ID);
        Assert.assertEquals(readValue.getBirthday(), CUSTOMER_BIRTHDAY);
    }

    @Test
    public void findSpecificCustomerAsUser() {
        BDDMockito.
            given(customerRepository.findOneById(CUSTOMER_ID)).
            willReturn(Optional.of(Customer.builder()
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
            .when().get(CUSTOMER_ENDPOINT + SPECIFIC_CUSTOMER_PATH, CUSTOMER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .name(CUSTOMER_NAME)
            .firstname(CUSTOMER_FIRSTNAME)
            .email(CUSTOMER_EMAIL)
            .birthday(CUSTOMER_BIRTHDAY)
            .build()));
    }

    @Test
    public void findSpecificNonExistingCustomerNotFoundAsUser() {
        BDDMockito.
            given(customerRepository.findOneById(CUSTOMER_ID)).
            willReturn(Optional.empty());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + SPECIFIC_CUSTOMER_PATH, CUSTOMER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }
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
    public void changeUserThenHTTPResponseOKAndChangedCustomerIsReturned() {
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
            .when().put(CUSTOMER_ENDPOINT + SPECIFIC_CUSTOMER_PATH, TEST_CUSTOMER_ID)
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
