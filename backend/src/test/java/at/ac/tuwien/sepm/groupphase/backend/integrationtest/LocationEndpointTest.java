package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class LocationEndpointTest extends BaseIntegrationTest {

    private static final String LOCATION_ENDPOINT = "/locations";

    private static final String LOCATION_FILTERED_NAME = "/locations?name=Name&page=0";
    private static final String LOCATION_FILTERED_COUNTRY = "/locations?country=Austria&page=0";
    private static final String LOCATION_FILTERED_POSTAL_CODE = "/locations?postalCode=1020&page=0";
    private static final String LOCATION_FILTERED_DESCRIPTION = "/locations?description=esc&page=0";
    private static final String LOCATION_FILTERED_STREET = "/locations?street=69&page=0";
    private static final String LOCATION_FILTERED_COUNTRY_AND_CITY = "/locations?country=Austria&city=Vienna&page=0";
    private static final String LOCATION_FILTERED_COUNTRY_AND_CITY_NOT_FOUND = "/locations?country=Austria&city=Innsbruck&page=0";

    private static final Long ID = 1L;
    private static final String NAME = "Name";
    private static final String COUNTRY = "Austria";
    private static final String CITY = "Vienna";
    private static final String POSTAL_CODE = "1020";
    private static final String STREET = "Street69";
    private static final String DESCRIPTION = "Description";


    @MockBean
    private LocationRepository locationRepository;

    @Test
    public void findAllLocationsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(LOCATION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findLocationByNameAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(NAME, null, null, null, null, null, PageRequest.of(0,10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Location.builder()
                        .locationName(NAME)
                        .id(ID)
                        .country(COUNTRY)
                        .city(CITY)
                        .postalCode(POSTAL_CODE)
                        .street(STREET)
                        .description(DESCRIPTION)
                        .build()),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_NAME)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        LocationDTO.builder()
                            .locationName(NAME)
                            .id(ID)
                            .country(COUNTRY)
                            .city(CITY)
                            .postalCode(POSTAL_CODE)
                            .street(STREET)
                            .description(DESCRIPTION)
                            .build()),
                    PageRequest.of(0,10), 1));

            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findLocationByCountryAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null, COUNTRY, null, null, null, null, PageRequest.of(0,10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                Location.builder()
                    .locationName(NAME)
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        LocationDTO.builder()
                            .locationName(NAME)
                            .id(ID)
                            .country(COUNTRY)
                            .city(CITY)
                            .postalCode(POSTAL_CODE)
                            .street(STREET)
                            .description(DESCRIPTION)
                            .build()),
                    PageRequest.of(0,10), 1));

            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findLocationByPostalCodeAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null,null, null, null, POSTAL_CODE, null, PageRequest.of(0,10)))
            .willReturn(new PageImpl<>(
            Collections.singletonList(
                Location.builder()
                    .locationName(NAME)
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()),
            PageRequest.of(0,10), 1)
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_POSTAL_CODE)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        LocationDTO.builder()
                            .locationName(NAME)
                            .id(ID)
                            .country(COUNTRY)
                            .city(CITY)
                            .postalCode(POSTAL_CODE)
                            .street(STREET)
                            .description(DESCRIPTION)
                            .build()),
                    PageRequest.of(0,10), 1));

            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

        @Test
        public void findLocationByDescriptionAsUser() {
            BDDMockito
                .given(locationRepository.findLocationsFiltered(null,null, null, null, null, "esc", PageRequest.of(0,10)))
                .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Location.builder()
                        .locationName(NAME)
                        .id(ID)
                        .country(COUNTRY)
                        .city(CITY)
                        .postalCode(POSTAL_CODE)
                        .street(STREET)
                        .description(DESCRIPTION)
                        .build()),
                PageRequest.of(0,10), 1)
            );

            Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
                .when().get(LOCATION_FILTERED_DESCRIPTION)
                .then().extract().response();

            Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
            try{
                String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                    new PageImpl<>(
                        Collections.singletonList(
                            LocationDTO.builder()
                                .locationName(NAME)
                                .id(ID)
                                .country(COUNTRY)
                                .city(CITY)
                                .postalCode(POSTAL_CODE)
                                .street(STREET)
                                .description(DESCRIPTION)
                                .build()),
                        PageRequest.of(0,10), 1));

                Assert.assertEquals(response.getBody().asString(), jsonObject);
            }catch (JsonProcessingException e) {
                Assert.fail();
            }
        }

        @Test
        public void findLocationByStreetAsUser() {
            BDDMockito.
                given(locationRepository.findLocationsFiltered(null,null, null, "69", null, null,PageRequest.of(0,10)))
                .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Location.builder()
                        .locationName(NAME)
                        .id(ID)
                        .country(COUNTRY)
                        .city(CITY)
                        .postalCode(POSTAL_CODE)
                        .street(STREET)
                        .description(DESCRIPTION)
                        .build()),
                PageRequest.of(0,10), 1)
            );

            Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
                .when().get(LOCATION_FILTERED_STREET)
                .then().extract().response();

            Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
            try{
                String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                    new PageImpl<>(
                        Collections.singletonList(
                            LocationDTO.builder()
                                .locationName(NAME)
                                .id(ID)
                                .country(COUNTRY)
                                .city(CITY)
                                .postalCode(POSTAL_CODE)
                                .street(STREET)
                                .description(DESCRIPTION)
                                .build()),
                        PageRequest.of(0,10), 1));

                Assert.assertEquals(response.getBody().asString(), jsonObject);
            }catch (JsonProcessingException e) {
                Assert.fail();
            }
        }

    @Test
    public void findLocationByCountryAndCityAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null, COUNTRY, CITY, null, null, null, PageRequest.of(0,10)))
           .willReturn(new PageImpl<>(
            Collections.singletonList(
                Location.builder()
                    .locationName(NAME)
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()),
            PageRequest.of(0,10), 1)
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY_AND_CITY)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        LocationDTO.builder()
                            .locationName(NAME)
                            .id(ID)
                            .country(COUNTRY)
                            .city(CITY)
                            .postalCode(POSTAL_CODE)
                            .street(STREET)
                            .description(DESCRIPTION)
                            .build()),
                    PageRequest.of(0,10), 1));

            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findSpecificNonExistingLocationNotFoundAsUser(){
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null, "Austria", "Innsbruck", null, null, null, PageRequest.of(0,10)))
            .willThrow(NotFoundException.class);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY_AND_CITY_NOT_FOUND)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void addLocationAsAdminThenHTTPResponseOKAndLocationIsReturned() {
        BDDMockito.
            given(locationRepository.save(any(Location.class))).
            willReturn(
                Location.builder()
                .locationName(NAME)
                .id(ID)
                .country(COUNTRY)
                .city(CITY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .description(DESCRIPTION)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                LocationDTO.builder()
                    .locationName(NAME)
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build())
            .when().post(LOCATION_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(LocationDTO.class), is(
            LocationDTO.builder()
            .locationName(NAME)
            .id(ID)
            .country(COUNTRY)
            .city(CITY)
            .postalCode(POSTAL_CODE)
            .street(STREET)
            .description(DESCRIPTION)
            .build()));
    }

    @Test
    public void deleteLocationAsAdminThenHTTPResponseOK() {
        BDDMockito.
            given(locationRepository.save(any(Location.class))).
            willReturn(
                Location.builder()
                    .locationName(NAME)
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().delete(LOCATION_ENDPOINT + "/1")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }
}