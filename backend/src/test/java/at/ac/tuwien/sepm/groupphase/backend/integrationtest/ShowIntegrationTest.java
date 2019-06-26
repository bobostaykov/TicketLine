package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class ShowIntegrationTest extends BaseIntegrationTest {
    private static final String SHOWS_SEARCH_ENDPOINT = "/shows/filter?";

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PricePatternRepository pricePatternRepository;
    @Autowired
    private ShowMapper showMapper;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


    private static final String LOCATION_NAME_1 = "Stadthalle";
    private static final String LOCATION_NAME_2 = "chez Horst";
    private static final String LOCATION_COUNTRY_1 = "Austria";
    private static final String LOCATION_COUNTRY_2 = "Germany";
    private static final String LOCATION_CITY_1 = "Tendlergasse";
    private static final String LOCATION_CITY_2 = "Bonn";
    private static final String LOCATION_STREET_1 = "Tendlergasse";
    private static final String LOCATION_STREET_2 = "Ermekeilstrasse";
    private static final Integer LOCATION_HOUSENR_1 = 66;
    private static final Integer LOCATION_HOUSENR_2 =  17;
    private static final String LOCATION_POSTALCODE_1 = "1090";
    private static final String LOCATION_POSTALCODE_2 = "53129";

    private static final String HALL_NAME_1 = "TopLocation";
    private static final String HALL_NAME_2 = "Konzertsaal";

    private static final String ARTIST_NAME_1 = "Zara Holland";
    private static final String ARTIST_NAME_2 = "Walter";

    private static final String EVENT_NAME_1 = "Birthday";
    private static final String EVENT_NAME_2 = "NoFind";
    private static final EventType EVENT_TYPE_1 = EventType.FESTIVAL;
    private static final EventType EVENT_TYPE_2 = EventType.CONCERT;
    private static final String EVENT_CONTENT_1 = "feiern";
    private static final String EVENT_CONTENT_2 = "warten";
    private static final String EVENT_DESCRIPTION_1 = "Its my Birthday";
    private static final String EVENT_DESCRIPTION_2 = "nicht da";
    private static final Integer EVENT_DURATION_1 = 180;
    private static final Integer EVENT_DURATION_2 = 400;

    private HashMap<PriceCategory, Double> priceMap1 = new HashMap<>();
    private HashMap<PriceCategory, Double> priceMap2 = new HashMap<>();

    private static final String PRICE_PATTERN_NAME_1 = "normal";
    private static final String PRICE_PATTERN_NAME_2 = "normal2";

    private static final Long TICKETS_SOLD = 10000L;
    private final LocalDate SHOW_DATE_1 = LocalDate.parse("17-03-2020", dateFormatter);
    private final LocalDate SHOW_DATE_2 = LocalDate.parse("15-10-2020", dateFormatter);
    private final LocalDate SHOW_DATE_3 = LocalDate.parse("15-10-2020", dateFormatter);
    private final LocalDate SHOW_DATE_4 = LocalDate.parse("15-10-2021", dateFormatter);
    private final LocalTime SHOW_TIME_1 = LocalTime.parse( "20:30", timeFormatter);
    private final LocalTime SHOW_TIME_2 = LocalTime.parse( "16:30", timeFormatter);
    private final LocalTime SHOW_TIME_3 = LocalTime.parse( "16:31", timeFormatter);
    private final LocalTime SHOW_TIME_4 = LocalTime.parse( "10:30", timeFormatter);
    private static final String SHOW_DESCRIPTION_1_3 = "description";
    private static final String SHOW_DESCRIPTION_4 = "Top-Abend";

    private Location locationAustria;
    private Location locationGermany;
    private Artist artist1;
    private Artist artist2;
    private Hall hall1;
    private Hall hall2;
    private Event event1;
    private Event event2;
    private Show show1;
    private Show show2;
    private Show show3;
    private Show show4;
    private PricePattern pricePattern1;
    private PricePattern pricePattern2;

    private static final String SEARCH_DATEFROM_QUERY = "dateFrom=";
    private static final String SEARCH_DATETO_QUERY = "dateTo=";
    private static final String SEARCH_TIMEFROM_QUERY = "timeFrom=";
    private static final String SEARCH_TIMETO_QUERY = "timeTo=";
    private static final String SEARCH_EVENTID_QUERY = "eventId=";

    private static final String SEARCH_EVENTNAME_QUERY = "eventName=";
    private static final String SEARCH_HALLNAME_QUERY = "hallName=";
    private static final String SEARCH_DURATION_QUERY = "duration=";
    private static final String SEARCH_COUNTRY_QUERY = "country=";
    private static final String SEARCH_CITY_QUERY = "city=";
    private static final String SEARCH_ARTISTNAME_QUERY = "artistName=";

    private static final String SEARCH_STREET_QUERY = "street=";
    private static final String SEARCH_HOUSENR_QUERY = "houseNr=";
    private static final String SEARCH_LOCATIONAME_QUERY = "locationName=";
    private static final String SEARCH_MINPRICE_QUERY = "minPrice=";
    private static final String SEARCH_MAXPRICE_QUERY = "maxPrice=";

    private static final String SEARCH_POSTALCODE_QUERY = "postalCode=";

    private static final String DATE_1 = "2020-03-18";
    private static final String DATE_2 = "2022-03-18";
    private static final String NAME_1 = "irthday";
    private static final String LOCATION_NAME_SEARCH_1 = "CheZ";
    private static final String MIN_PRICE = "30";
    private static final String MAX_PRICE = "30";
    private boolean init = false;




    @Before
    public void before(){
        showRepository.deleteAll();
        eventRepository.deleteAll();
        hallRepository.deleteAll();
        locationRepository.deleteAll();
        artistRepository.deleteAll();
        pricePatternRepository.deleteAll();

            priceMap1.put(PriceCategory.CHEAP, 10.0); priceMap1.put(PriceCategory.AVERAGE, 20.0); priceMap1.put(PriceCategory.EXPENSIVE, 40.0);
            pricePattern1 = pricePatternRepository.save(PricePattern.builder().setName(PRICE_PATTERN_NAME_1).setPriceMapping(priceMap1).createPricePattern());
            priceMap2.put(PriceCategory.CHEAP, 10.0); priceMap2.put(PriceCategory.AVERAGE, 15.0); priceMap2.put(PriceCategory.EXPENSIVE, 20.0);
            pricePattern2 = pricePatternRepository.save(PricePattern.builder().setName(PRICE_PATTERN_NAME_2).setPriceMapping(priceMap2).createPricePattern());
            locationAustria = locationRepository.save(Location.builder().locationName(LOCATION_NAME_1).city(LOCATION_CITY_1).country(LOCATION_COUNTRY_1).postalCode(LOCATION_POSTALCODE_1).street(LOCATION_STREET_1).postalCode(LOCATION_POSTALCODE_1).build());
            locationGermany = locationRepository.save(Location.builder().locationName(LOCATION_NAME_2).city(LOCATION_CITY_2).country(LOCATION_COUNTRY_2).postalCode(LOCATION_POSTALCODE_2).street(LOCATION_STREET_2).postalCode(LOCATION_POSTALCODE_2).build());
            artist1 = artistRepository.save(Artist.builder().name(ARTIST_NAME_1).build());
            artist2 = artistRepository.save(Artist.builder().name(ARTIST_NAME_2).build());
            hall1 = hallRepository.save(Hall.builder().name(HALL_NAME_1).location(locationAustria).build());
            hall2 = hallRepository.save(Hall.builder().name(HALL_NAME_2).location(locationGermany).build());
            event1 = eventRepository.save(Event.builder().name(EVENT_NAME_1).eventType(EVENT_TYPE_1).artist(artist1).content(EVENT_CONTENT_1).description(EVENT_DESCRIPTION_1).durationInMinutes(EVENT_DURATION_1).build());
            event2 = eventRepository.save(Event.builder().name(EVENT_NAME_2).eventType(EVENT_TYPE_2).artist(artist2).content(EVENT_CONTENT_2).description(EVENT_DESCRIPTION_2).durationInMinutes(EVENT_DURATION_2).build());
            show1 = showRepository.save(Show.builder().event(event1).pricePattern(pricePattern1).ticketsSold(TICKETS_SOLD).time(SHOW_TIME_1).date(SHOW_DATE_1).hall(hall1).description(SHOW_DESCRIPTION_1_3).build());
            show2 = showRepository.save(Show.builder().event(event1).pricePattern(pricePattern1).ticketsSold(TICKETS_SOLD).time(SHOW_TIME_2).date(SHOW_DATE_2).hall(hall1).description(SHOW_DESCRIPTION_1_3).build());
            show3 = showRepository.save(Show.builder().event(event2).pricePattern(pricePattern1).ticketsSold(TICKETS_SOLD).time(SHOW_TIME_3).date(SHOW_DATE_3).hall(hall1).description(SHOW_DESCRIPTION_1_3).build());
            show4 = showRepository.save(Show.builder().event(event2).pricePattern(pricePattern2).ticketsSold(TICKETS_SOLD).time(SHOW_TIME_4).date(SHOW_DATE_4).hall(hall2).description(SHOW_DESCRIPTION_4).build());
            init = true;
        }





    @Test
    public void findShowsByStartDate_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_DATEFROM_QUERY + DATE_1 + "&page=0")
            .then().extract().response();
        response.print();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 3);
    }

    @Test
    public void findShowsByEventName_findsFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_EVENTNAME_QUERY + NAME_1 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 2);
    }

    @Test
    public void findShowsByArtistName_findsFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_ARTISTNAME_QUERY + ARTIST_NAME_1 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 2);
    }

    @Test
    public void searchShowsByMaxDate_givenNoMatchingCriteria_throwsNotFoundException(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_DATEFROM_QUERY + DATE_2 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void searchShowsByHallName_returnsCorrectNumberOfShows() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_HALLNAME_QUERY + HALL_NAME_1 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 3);
    }

    @Test
    public void searchShowsByDuration_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_DURATION_QUERY + EVENT_DURATION_1 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 2);
    }

    @Test
    public void searchShowsByLocationName_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_LOCATIONAME_QUERY + LOCATION_NAME_SEARCH_1 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 1);
    }

    @Test
    public void searchShowsByCityName_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_CITY_QUERY + locationGermany.getCity() + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 1);
    }

    @Test
    public void searchShowsByStreet_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_STREET_QUERY + LOCATION_STREET_2 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 1);
    }

    @Test
    public void searchShowsByPostalCode_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_POSTALCODE_QUERY + locationGermany.getPostalCode() + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 1);
    }

    @Test
    public void searchShowsByCountry_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_COUNTRY_QUERY + LOCATION_COUNTRY_2 + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 1);
    }

    @Test
    public void searchShowsByMinPrice_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_MINPRICE_QUERY + MIN_PRICE + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(shows.size(), 3);
    }

    @Test
    public void searchShowsByMaxPrice_returnsCorrectNumberOfShows(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(SHOWS_SEARCH_ENDPOINT + SEARCH_MAXPRICE_QUERY + MAX_PRICE + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<Show> shows = response.jsonPath().getList("content");
        Assert.assertEquals(4, shows.size() );
    }
}
