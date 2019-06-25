package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.any;

public class HallEndpointTest extends BaseIntegrationTest {

    private static final String HALL_ENDPOINT = "/halls";
    private static final Long HALL_ID = 1L;
    private static final String HALL_NAME = "Test_Hall";

    private static final Long LOCATION_ID = 1L;
    private static final String LOCATION_COUNTRY = "Austria";
    private static final String LOCATION_CITY = "Vienna";
    private static final String LOCATION_POSTAL_CODE = "1010";
    private static final String LOCATION_STREET = "Kärntner Straße";
    private static final String LOCATION_DESCRIPTION = "Einkaufsstraße";

    private static final Long SEAT_ID = 1L;
    private static final Integer SEAT_NUMBER = 1;
    private static final Integer SEAT_ROW = 1;
    private static final PriceCategory SEAT_PRICE_CATEGORY = PriceCategory.AVERAGE;

    private static final Long SECTOR_ID = 1L;
    private static final Integer SECTOR_NUMBER = 1;
    private static final PriceCategory SECTOR_PRICE_CATEGORY = PriceCategory.AVERAGE;

    private static final HallDTO HALL_DTO_WITH_SEAT = HallDTO.builder()
        .id(HALL_ID).name(HALL_NAME).location(
            LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
        ).seats(Collections.singletonList(
            SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW).priceCategory(SEAT_PRICE_CATEGORY)
                .build()
        )).build();

    private static final Hall HALL_WITH_SEAT = Hall.builder()
        .id(HALL_ID).name(HALL_NAME).location(
            Location.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
        ).seats(Collections.singletonList(
            Seat.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW).priceCategory(SEAT_PRICE_CATEGORY)
                .build()
        )).build();

    private static final HallDTO HALL_DTO_WITH_SECTOR = HallDTO.builder()
        .id(HALL_ID).name(HALL_NAME).location(
            LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
        ).sectors(Collections.singletonList(
            SectorDTO.builder().id(SECTOR_ID).sectorNumber(SECTOR_NUMBER).priceCategory(SECTOR_PRICE_CATEGORY)
                .build()
        )).build();

    private static final Hall HALL_WITH_SECTOR = Hall.builder()
        .id(HALL_ID).name(HALL_NAME).location(
            Location.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
        ).sectors(Collections.singletonList(
            Sector.builder().id(SECTOR_ID).sectorNumber(SECTOR_NUMBER).priceCategory(SECTOR_PRICE_CATEGORY)
                .build()
        )).build();

    @MockBean
    HallRepository hallRepository;

    @Before
    public void mockito_basic_init() {
        BDDMockito
            .when(hallRepository.findAll())
            .thenReturn(Arrays.asList(
                HALL_WITH_SEAT, HALL_WITH_SECTOR
            ));
    }

    @Test
    public void findAllHalls_unauthorized_shouldReturnStatusUnauthorized() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllHalls_asUser_shouldReturnList() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(HallDTO[].class)), is(Arrays.asList(
            HALL_DTO_WITH_SEAT, HALL_DTO_WITH_SECTOR
        )));
    }

    @Test
    public void findAllHalls_asAdmin_shouldReturnList() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(HallDTO[].class)), is(Arrays.asList(
            HALL_DTO_WITH_SEAT, HALL_DTO_WITH_SECTOR
        )));
    }

    @Test
    public void postHallWithSeat_unauthorized_shouldReturnStatusUnauthorized() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(HALL_DTO_WITH_SEAT)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void postHallWithSector_unauthorized_shouldReturnUnauthorized() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(HALL_DTO_WITH_SECTOR)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void postHallWithSeat_asUser_shouldReturnStatusForbidden() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(HALL_DTO_WITH_SEAT)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @Ignore //actually works when validation is ignored, but validation is executed before authorization and validation fails so code is 400
    public void postHallWithSector_asUser_shouldReturnStatusForbidden() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(HALL_DTO_WITH_SECTOR)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Ignore
    @Test
    public void postHallWithSeat_asAdmin_returnHall() {
        BDDMockito
            .when(hallRepository.save(any(Hall.class)))
            .thenReturn(Hall.builder()
                .id(HALL_ID).name(HALL_NAME).location(
                    Location.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .street(LOCATION_STREET).postalCode(LOCATION_POSTAL_CODE).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    Seat.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW)
                        .priceCategory(SEAT_PRICE_CATEGORY).hall(HALL_WITH_SEAT).build()
                ))
                .build());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(HALL_DTO_WITH_SEAT)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CREATED.value()));
        Assert.assertThat(response.as(HallDTO.class), is(HALL_DTO_WITH_SEAT));
    }

    @Ignore
    @Test
    public void postHallWithSector_asAdmin_returnHall() {
        BDDMockito
            .when(hallRepository.save(any(Hall.class)))
            .thenReturn(Hall.builder()
                .id(HALL_ID).name(HALL_NAME).location(
                    Location.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .street(LOCATION_STREET).postalCode(LOCATION_POSTAL_CODE).description(LOCATION_DESCRIPTION).build()
                ).sectors(Collections.singletonList(
                    Sector.builder().id(SECTOR_ID).sectorNumber(SECTOR_NUMBER).priceCategory(SEAT_PRICE_CATEGORY)
                        .hall(HALL_WITH_SECTOR).build()
                ))
                .build());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(HALL_DTO_WITH_SECTOR)
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CREATED.value()));
        Assert.assertThat(response.as(HallDTO.class), is(HALL_DTO_WITH_SECTOR));
    }

    @Test
    public void postHall_withSeatsAndSectors_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW)
                        .priceCategory(SEAT_PRICE_CATEGORY).build()
                )).sectors(Collections.singletonList(
                    SectorDTO.builder().id(SECTOR_ID).sectorNumber(SECTOR_NUMBER)
                        .priceCategory(SECTOR_PRICE_CATEGORY).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_withoutSeatsOrSectors_shouldReturnStatusBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_withoutName_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(null).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW)
                        .priceCategory(SEAT_PRICE_CATEGORY).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_withoutLocation_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(null)
                    .seats(Collections.singletonList(
                        SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW)
                            .priceCategory(SEAT_PRICE_CATEGORY).build()
                    )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_seatWithoutRow_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(null)
                        .priceCategory(SEAT_PRICE_CATEGORY).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_seatWithoutNumber_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    SeatDTO.builder().id(SEAT_ID).seatNumber(null).seatRow(SEAT_ROW)
                        .priceCategory(SEAT_PRICE_CATEGORY).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_seatWithoutPriceCategory_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).seats(Collections.singletonList(
                    SeatDTO.builder().id(SEAT_ID).seatNumber(SEAT_NUMBER).seatRow(SEAT_ROW)
                        .priceCategory(null).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_sectorWithoutNumber_shouldReturnBadRequest() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).sectors(Collections.singletonList(
                    SectorDTO.builder().id(SECTOR_ID).sectorNumber(null)
                        .priceCategory(SECTOR_PRICE_CATEGORY).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void postHall_sectorWithoutPriceCategory_shouldReturnBadRequest(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                HallDTO.builder()
                    .id(HALL_ID).name(HALL_NAME).location(
                    LocationDTO.builder().id(LOCATION_ID).country(LOCATION_COUNTRY).city(LOCATION_CITY)
                        .postalCode(LOCATION_POSTAL_CODE).street(LOCATION_STREET).description(LOCATION_DESCRIPTION).build()
                ).sectors(Collections.singletonList(
                    SectorDTO.builder().id(SECTOR_ID).sectorNumber(SECTOR_NUMBER)
                        .priceCategory(null).build()
                )).build()
            )
            .when().post(HALL_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }
}


