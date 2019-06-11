package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.TopTenDetailsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.*;

import static org.hamcrest.core.Is.is;

public class EventEndpointTest extends BaseIntegrationTest {

    private static final String EVENT_ENDPOINT = "/events";
    private static final String TOP_TEN_EVENTS_PATH= "/topten";
    private static final String MONTHS = "January";
    private static final String CATEGORIES = "OPERA,FESTIVAL,THEATRE";
    private static final Long TEST_EVENT_ID = 1L;
    private static final String TEST_EVENT_NAME = "Test Event";
    private static final EventType TEST_EVENT_TYPE = EventType.FESTIVAL;
    private static final Long TEST_TICKETS_SOLD = 10000L;
    private static Set<String> monthsSet = new HashSet<>(Arrays.asList(MONTHS.split(",")));
    private static List<String> monthsList = Arrays.asList(MONTHS.split(","));
    private static Set<EventType> categoriesSet = new HashSet<>(Arrays.asList(EventType.valueOf("OPERA"), EventType.valueOf("FESTIVAL"), EventType.valueOf("THEATRE")));
    private static List<String> categoriesList = Arrays.asList(CATEGORIES.split(","));

    @MockBean
    private EventRepository eventRepository;

    @Test
    public void findTopTenEventsUnauthorisedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().post(EVENT_ENDPOINT + TOP_TEN_EVENTS_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findTopTenEventsAsUser() {
        BDDMockito
            .given(eventRepository.findTopTenEvents(monthsSet, categoriesSet))
            .willReturn(Collections.singletonList(new Tuple() {
                @Override
                public <X> X get(TupleElement<X> tupleElement) {
                    return null;
                }

                @Override
                public <X> X get(String s, Class<X> aClass) {
                    return null;
                }

                @Override
                public Object get(String s) {
                    return null;
                }

                @Override
                public <X> X get(int i, Class<X> aClass) {
                    return null;
                }

                @Override
                public Object get(int i) {
                    List<Object> list = new ArrayList<>(){};
                    list.add(TEST_EVENT_NAME);
                    list.add(TEST_TICKETS_SOLD);
                    return list.get(i);
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public List<TupleElement<?>> getElements() {
                    return null;
                }
            }));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(TopTenDetailsDTO.builder()
                .months(monthsList)
                .categories(categoriesList)
                .build())
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().post(EVENT_ENDPOINT + TOP_TEN_EVENTS_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(EventTicketsDTO[].class)), is(Collections.singletonList(
            EventTicketsDTO.builder()
                .eventName(TEST_EVENT_NAME)
                .ticketsSold(TEST_TICKETS_SOLD)
                .build())));
    }

    @Test
    public void findTopTenEventsAsAdmin() {
        BDDMockito
            .given(eventRepository.findTopTenEvents(monthsSet, categoriesSet))
            .willReturn(Collections.singletonList(new Tuple() {
                @Override
                public <X> X get(TupleElement<X> tupleElement) {
                    return null;
                }

                @Override
                public <X> X get(String s, Class<X> aClass) {
                    return null;
                }

                @Override
                public Object get(String s) {
                    return null;
                }

                @Override
                public <X> X get(int i, Class<X> aClass) {
                    return null;
                }

                @Override
                public Object get(int i) {
                    List<Object> list = new ArrayList<>(){};
                    list.add(TEST_EVENT_NAME);
                    list.add(TEST_TICKETS_SOLD);
                    return list.get(i);
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public List<TupleElement<?>> getElements() {
                    return null;
                }
            }));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(TopTenDetailsDTO.builder()
                .months(monthsList)
                .categories(categoriesList)
                .build())
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(EVENT_ENDPOINT + TOP_TEN_EVENTS_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(EventTicketsDTO[].class)), is(Collections.singletonList(
            EventTicketsDTO.builder()
                .eventName(TEST_EVENT_NAME)
                .ticketsSold(TEST_TICKETS_SOLD)
                .build())));
    }

    @Test
    public void findAllEventsUnauthorisedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllEventsAsUser() {
        BDDMockito
            .given(eventRepository.findAllByOrderByNameAsc())
            .willReturn(Collections.singletonList(
                Event.builder()
                    .id(TEST_EVENT_ID)
                    .name(TEST_EVENT_NAME)
                    .eventType(TEST_EVENT_TYPE)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType    .JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(EventDTO[].class)), is(Collections.singletonList(
            EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .build())));
    }

    @Test
    public void findAllEventsAsAdmin() {
        BDDMockito
            .given(eventRepository.findAllByOrderByNameAsc())
            .willReturn(Collections.singletonList(
                Event.builder()
                    .id(TEST_EVENT_ID)
                    .name(TEST_EVENT_NAME)
                    .eventType(TEST_EVENT_TYPE)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(EventDTO[].class)), is(Collections.singletonList(
            EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .build())));
    }

}
