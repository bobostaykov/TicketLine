package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.hall.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class ShowEndpointTest extends BaseIntegrationTest {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static final String SHOWS_ENDPOINT = "/shows";
    private static final Long ID = 1L;
    private static final String DESCRIPTION = "Description";
    private final LocalDate DATE = LocalDate.parse("15-10-2021", dateFormatter);
    private final LocalTime TIME = LocalTime.parse( "20:30", timeFormatter);
    private static final Long TICKETS_SOLD = 1000L;

    private static final HashMap<PriceCategory, Double> PRICE_MAP = new HashMap<>() {{
        put(PriceCategory.CHEAP, 10.0);
        put(PriceCategory.AVERAGE, 20.0);
        put(PriceCategory.EXPENSIVE, 40.0);
    }};

    private static final PricePattern PRICE_PATTERN = PricePattern.builder().setName("PricePattern").setPriceMapping(PRICE_MAP).createPricePattern();

    private static final Artist ARTIST = Artist.builder()
        .id(1L)
        .name("Artist")
        .build();

    private static final Location LOCATION = Location.builder()
        .id(1L)
        .locationName("Location")
        .country("Country")
        .city("City")
        .street("Street")
        .postalCode("PostalCode")
        .description("Description")
        .build();

    private static final Hall HALL = Hall.builder()
        .id(1L)
        .name("Hall")
        .location(LOCATION)
        .seats(null)
        .sectors(null)
        .build();

    private static final Event EVENT = Event.builder()
        .id(1L)
        .name("Event")
        .eventType(EventType.OPERA)
        .artist(ARTIST)
        .durationInMinutes(100)
        .description("Description")
        .content("Content")
        .build();

    @MockBean
    private ShowRepository showRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Test
    public void findAllShowsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(SHOWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void addShowAsAdminThenHTTPResponseOKAndShowIsReturned() {
        BDDMockito.
            given(showRepository.save(any(Show.class)))
            .willReturn(
                Show.builder()
                    .id(ID)
                    .event(EVENT)
                    .hall(HALL)
                    .date(DATE)
                    .time(TIME)
                    .description(DESCRIPTION)
                    .ticketsSold(TICKETS_SOLD)
                    .pricePattern(PRICE_PATTERN)
                    .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(
                ShowDTO.builder()
                    .id(ID)
                    .event(eventMapper.eventToEventDTO(EVENT))
                    .hall(hallMapper.hallToHallDTO(HALL))
                    .date(DATE)
                    .time(TIME)
                    .description(DESCRIPTION)
                    .ticketsSold(TICKETS_SOLD)
                    .pricePattern(PRICE_PATTERN)
                    .build())
            .when().post(SHOWS_ENDPOINT)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        ShowDTO readValue = response.as(ShowDTO.class);
        Assert.assertNotNull(readValue);
        Assert.assertEquals(readValue.getId(), ID);
        Assert.assertEquals(readValue.getEvent(), eventMapper.eventToEventDTO(EVENT));
        Assert.assertEquals(readValue.getHall(), hallMapper.hallToHallDTO(HALL));
        Assert.assertEquals(readValue.getDate(), DATE);
        Assert.assertEquals(readValue.getTime(), TIME);
        Assert.assertEquals(readValue.getTicketsSold(), TICKETS_SOLD);
        Assert.assertEquals(readValue.getDescription(), DESCRIPTION);
        Assert.assertEquals(readValue.getPricePattern().getId(), PRICE_PATTERN.getId());
        Assert.assertEquals(readValue.getPricePattern().getName(), PRICE_PATTERN.getName());
        Assert.assertEquals(readValue.getPricePattern().getPriceMapping(), PRICE_PATTERN.getPriceMapping());
    }


    @Test
    public void deleteShowAsAdminThenHTTPResponseOK() {
        BDDMockito.
            given(showRepository.save(any(Show.class)))
            .willReturn(
                Show.builder()
                    .id(ID)
                    .event(EVENT)
                    .hall(HALL)
                    .date(DATE)
                    .time(TIME)
                    .description(DESCRIPTION)
                    .ticketsSold(TICKETS_SOLD)
                    .pricePattern(PRICE_PATTERN)
                    .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().delete(SHOWS_ENDPOINT + "/1")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }
}
