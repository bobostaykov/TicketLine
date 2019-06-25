package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class EventIntegrationTest extends BaseIntegrationTest {


    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ShowRepository showRepository;

    private static final String EVENTS_SEARCH_ENDPOINT = "/events/?";
    private boolean initiated = false;
    private Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
    private Artist artist2 = Artist.builder().id(2L).name("Anton aus Tirol").build();

    private Event event1 = Event.builder().name("Birthday").eventType(EventType.FESTIVAL).content("feiern").description("Its my Birthday").artist(artist1).durationInMinutes(180).build();
    private Event event2 = Event.builder().name("NoFind").eventType(EventType.CONCERT).content("feiern und warten").description("nicht da").artist(artist1).durationInMinutes(400).build();
    private Event event3 = Event.builder().name("Birthday").eventType(EventType.FESTIVAL).content("feiern").description("Its my Birthday").artist(artist2).durationInMinutes(400).build();
    private Event eventTheatre = Event.builder().name("NOTAASDLKSAKDJl").eventType(EventType.THEATRE).content("SAÖLDKASKDAÖLSKD").description("SADKÖSADKLASJDKL").artist(artist2).durationInMinutes(5000).build();
    String EVENT_NAME_SEARCH =  EVENTS_SEARCH_ENDPOINT +"eventName=";
    String CONTENT_SEARCH =  EVENTS_SEARCH_ENDPOINT +"content=";
    String DURATION_SEARCH =  EVENTS_SEARCH_ENDPOINT +"duration=";
    String ARTIST_NAME_SEARCH =  EVENTS_SEARCH_ENDPOINT +"artistName=";
    String EVENT_TYPE_SEARCH =  EVENTS_SEARCH_ENDPOINT +"eventType=";

    @Before
    public void before(){
        if(!initiated){
            this.showRepository.deleteAll();
            this.eventRepository.deleteAll();
            this.artistRepository.deleteAll();

            artist1 = artistRepository.save(artist1);
            artist2 = artistRepository.save(artist2);
            event1.setArtist(artist1);
            event2.setArtist(artist1);
            event3.setArtist(artist2);
            eventTheatre.setArtist(artist2);
            event1 = eventRepository.save(event1);
            event2 = eventRepository.save(event2);
            event3 = eventRepository.save(event3);
            eventTheatre = eventRepository.save(eventTheatre);
            initiated = true;
        }
    }
    @After
    public void after(){
        this.eventRepository.deleteAll();
        this.artistRepository.deleteAll();
        this.initiated = false;
    }


      @Test
    public void findEventsByName_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_NAME_SEARCH + "Birthday" + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<EventDTO> eventDTOS = response.jsonPath().getList("content");
        Assert.assertEquals(2, eventDTOS.size()  );
    }
    @Test
    public void findEventsByContent_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CONTENT_SEARCH + "feiern" + "&page=0")
            .then().extract().response();
        response.print();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<EventDTO> events = response.jsonPath().getList("content");
        Assert.assertEquals(events.size(), 3);
        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CONTENT_SEARCH + "feiern" + "&page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        events = response.jsonPath().getList("content");
        Assert.assertEquals(3, events.size() );
    }

    @Test
    public void findEventsByDuration_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(DURATION_SEARCH + "160" + "&page=0")
            .then().extract().response();
        response.print();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<EventDTO> events = response.jsonPath().getList("content");
        Assert.assertEquals( events.size(), 1);
        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(DURATION_SEARCH + "420" + "&page=0")
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        events = response.jsonPath().getList("content");
        Assert.assertEquals(2 ,events.size());
    }

    public void findEventsByArtist_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(ARTIST_NAME_SEARCH + artist1.getName() + "&page=0")
            .then().extract().response();
        response.print();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<EventDTO> events = response.jsonPath().getList("content");
        Assert.assertEquals(events.size(), 2);
        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(ARTIST_NAME_SEARCH + artist2.getName() + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        events = response.jsonPath().getList("content");
        Assert.assertEquals(events.size(), 2);
    }
    public void findEventsByEventType_findsCorrectNumberOfFittingEvents(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_TYPE_SEARCH + event3.getEventType() + "&page=0")
            .then().extract().response();
        response.print();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<EventDTO> events = response.jsonPath().getList("content");
        Assert.assertEquals(events.size(), 2);
        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_TYPE_SEARCH + eventTheatre.getEventType() + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        events = response.jsonPath().getList("content");
        Assert.assertEquals(events.size(), 2);
    }

    @Test
    public void searchByEventName_Throws_Not_Found_Status_When_Not_Found(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_NAME_SEARCH + "Rihanna" + "&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }


}
