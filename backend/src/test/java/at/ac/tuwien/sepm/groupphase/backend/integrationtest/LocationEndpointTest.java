package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
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
import java.util.List;

import static org.hamcrest.core.Is.is;

//TODO understand and fix tests
public class LocationEndpointTest extends BaseIntegrationTest {

    private static final String LOCATION_ENDPOINT = "/locations";

    private static final String LOCATION_FILTERED_COUNTRY = "/locations?country=Austria";
    private static final String LOCATION_FILTERED_DESCRIPTION = "/locations?description=esc";
    private static final String LOCATION_FILTERED_STREET = "/locations?street=69";
    private static final String LOCATION_FILTERED_COUNTRY_AND_CITY = "/locations?country=Austria&city=Vienna";
    private static final String LOCATION_FILTERED_COUNTRY_AND_CITY_NOT_FOUND = "/locations?country=Austria&city=Graz";

    private static final Long ID = 1L;
    private static final String COUNTRY = "Austria";
    private static final String CITY = "Vienna";
    private static final String POSTAL_CODE = "1020";
    private static final String STREET = "Mumbo Jumbo Str. 69";
    private static final String DESCRIPTION = "Description";


    @MockBean
    private LocationRepository locationRepository;

    @Test
    public void findAllNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(LOCATION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findLocationByCountryAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(COUNTRY, null, null, null, null)).
            willReturn(Collections.singletonList(
                Location.builder()
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(LocationDTO[].class)), is(Collections.singletonList(
            LocationDTO.builder()
                .id(ID)
                .country(COUNTRY)
                .city(CITY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .description(DESCRIPTION)
                .build())));
    }

    @Test
    public void findLocationByDescriptionAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null, null, null, null, DESCRIPTION)).
            willReturn(Collections.singletonList(
                Location.builder()
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_DESCRIPTION)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(LocationDTO[].class)), is(Collections.singletonList(
            LocationDTO.builder()
                .id(ID)
                .country(COUNTRY)
                .city(CITY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .description(DESCRIPTION)
                .build())));
    }

    @Test
    public void findLocationByStreetAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(null, null, STREET, null, null)).
            willReturn(Collections.singletonList(
                Location.builder()
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_STREET)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(LocationDTO[].class)), is(Collections.singletonList(
            LocationDTO.builder()
                .id(ID)
                .country(COUNTRY)
                .city(CITY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .description(DESCRIPTION)
                .build())));
    }

    @Test
    public void findLocationByCountryAndCityAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(COUNTRY, CITY, null, null, null)).
            willReturn(Collections.singletonList(
                Location.builder()
                    .id(ID)
                    .country(COUNTRY)
                    .city(CITY)
                    .postalCode(POSTAL_CODE)
                    .street(STREET)
                    .description(DESCRIPTION)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY_AND_CITY)
            .then().extract().response();
        Assert.assertThat(Arrays.asList(response.as(LocationDTO[].class)), is(Collections.singletonList(
            LocationDTO.builder()
                .id(ID)
                .country(COUNTRY)
                .city(CITY)
                .postalCode(POSTAL_CODE)
                .street(STREET)
                .description(DESCRIPTION)
                .build())));
    }

    @Test
    public void findSpecificNonExistingLocationNotFoundAsUser() {
        BDDMockito.
            given(locationRepository.findLocationsFiltered(COUNTRY, CITY, null, null, null)).
            willReturn(List.of());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(LOCATION_FILTERED_COUNTRY_AND_CITY_NOT_FOUND)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }
}
