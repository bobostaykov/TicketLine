package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.TopTenDetailsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
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

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class EventEndpointTest extends BaseIntegrationTest {

    private static final String EVENT_ENDPOINT = "/events";
    private static final String TOP_TEN_EVENTS_PATH = "/topten";
    private static final String MONTHS = "January";
    private static final String CATEGORIES = "OPERA,FESTIVAL,THEATRE";
    private static final Long TEST_EVENT_ID = 1L;
    private static final Integer TEST_EVENT_DURATION = 120;
    private static final String TEST_EVENT_NAME = "Test Event";
    private static final EventType TEST_EVENT_TYPE = EventType.FESTIVAL;
    private static final Long TEST_TICKETS_SOLD = 10000L;
    private static final String TEST_ARTIST_NAME = "Test Artist";
    private static final Long TEST_ARTIST_ID= 1L;
    private static Set<String> monthsSet = new HashSet<>(Arrays.asList(MONTHS.split(",")));
    private static List<String> monthsList = Arrays.asList(MONTHS.split(","));
    private static Set<EventType> categoriesSet = new HashSet<>(Arrays.asList(EventType.valueOf("OPERA"), EventType.valueOf("FESTIVAL"), EventType.valueOf("THEATRE")));
    private static List<String> categoriesList = Arrays.asList(CATEGORIES.split(","));

    private static final String EVENT_FILTERED_ALL = "/events?page=0";
    private static final String EVENT_FILTERED_NAME = "/events?eventName=a&page=0";
    private static final String EVENT_FILTERED_TYPE = "/events?eventType=OPERA&page=0";
    private static final String EVENT_FILTERED_DESCRIPTION = "/events?description=esc&page=0";
    private static final String EVENT_FILTERED_CONTENT = "/events?content=NO_CONTENT&page=0";
    private static final String EVENT_FILTERED_DURATION = "/events?duration=200&page=0";
    private static final String EVENT_FILTERED_ARTIST_NAME = "/events?artistName=vasko&page=0";
    private static final String EVENT_FILTERE_ARTIST_ID = "/events/artist/2?page=0";

    private static final Artist ARTIST_1 = Artist.builder().id(1L).name("Vasko").build();
    private static final Artist ARTIST_2 = Artist.builder().id(2L).name("Venci").build();
    private static final ArtistDTO ARTIST_DTO_1 = ArtistDTO.builder().id(1L).name("Vasko").build();
    private static final ArtistDTO ARTIST_DTO_2 = ArtistDTO.builder().id(2L).name("Venci").build();

    private static final Long ID_EVENT_1 = 1L;
    private static final String NAME_1 = "EVENT_A";
    private static final EventType TYPE_1 = EventType.THEATRE;
    private static final String DESCRIPTION_1 = "Description";
    private static final String CONTENT_1 = "NotFound";
    private static final Integer DURATION_1 = 210;

    private static final Long ID_EVENT_2 = 2L;
    private static final String NAME_2 = "EVENT_B";
    private static final EventType TYPE_2 = EventType.OPERA;
    private static final String DESCRIPTION_2 = "NotFound";
    private static final String CONTENT_2 = "NotFound";
    private static final Integer DURATION_2 = 190;

    @MockBean
    private EventRepository eventRepository;

    @Test
    public void createEventUnauthorisedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void createEventAsUser() {
        BDDMockito
            .given(eventRepository.save(any(Event.class)))
            .willReturn(Event.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .durationInMinutes(TEST_EVENT_DURATION)
                .artist(Artist.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .durationInMinutes(TEST_EVENT_DURATION)
                .artist(ArtistDTO.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
                .build())
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CREATED.value()));
        Assert.assertThat(response.as(EventDTO.class), is(EventDTO.builder()
            .id(TEST_EVENT_ID)
            .name(TEST_EVENT_NAME)
            .eventType(TEST_EVENT_TYPE)
            .durationInMinutes(TEST_EVENT_DURATION)
            .artist(ArtistDTO.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
            .build()));
    }

    @Test
    public void createEventAsAdmin() {
        BDDMockito
            .given(eventRepository.save(any(Event.class)))
            .willReturn(Event.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .durationInMinutes(TEST_EVENT_DURATION)
                .artist(Artist.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(EventDTO.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .durationInMinutes(TEST_EVENT_DURATION)
                .artist(ArtistDTO.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
                .build())
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CREATED.value()));
        Assert.assertThat(response.as(EventDTO.class), is(EventDTO.builder()
            .id(TEST_EVENT_ID)
            .name(TEST_EVENT_NAME)
            .eventType(TEST_EVENT_TYPE)
            .durationInMinutes(TEST_EVENT_DURATION)
            .artist(ArtistDTO.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
            .build()));
    }

    @Test
    public void deleteEventAsAdminThenHTTPResponseOK() {
        BDDMockito.
            given(eventRepository.save(any(Event.class)))
            .willReturn(Event.builder()
                .id(TEST_EVENT_ID)
                .name(TEST_EVENT_NAME)
                .eventType(TEST_EVENT_TYPE)
                .durationInMinutes(TEST_EVENT_DURATION)
                .artist(Artist.builder().name(TEST_ARTIST_NAME).id(TEST_ARTIST_ID).build())
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().delete(EVENT_ENDPOINT + "/1")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

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
                    List<Object> list = new ArrayList<>() {};
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
                    List<Object> list = new ArrayList<>() {
                    };
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
    public void findAllEventsOrderedByNameAscAsUser() {
        BDDMockito.
            given(eventRepository.findAllByOrderByNameAsc(PageRequest.of(0,10)))
            .willReturn(new PageImpl<>(
                List.of(
                    Event.builder()
                        .id(ID_EVENT_1)
                        .name(NAME_1)
                        .eventType(TYPE_1)
                        .description(DESCRIPTION_1)
                        .content(CONTENT_1)
                        .durationInMinutes(DURATION_1)
                        .artist(ARTIST_1)
                        .build(),
                    Event.builder()
                        .id(ID_EVENT_2)
                        .name(NAME_2)
                        .eventType(TYPE_2)
                        .description(DESCRIPTION_2)
                        .content(CONTENT_2)
                        .durationInMinutes(DURATION_2)
                        .artist(ARTIST_2)
                        .build()
                ),
                PageRequest.of(0,10), 2)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_ALL)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Arrays.asList(
                        EventDTO.builder()
                            .id(ID_EVENT_1)
                            .name(NAME_1)
                            .eventType(TYPE_1)
                            .description(DESCRIPTION_1)
                            .content(CONTENT_1)
                            .durationInMinutes(DURATION_1)
                            .artist(ARTIST_DTO_1)
                            .build(),
                        EventDTO.builder()
                            .id(ID_EVENT_2)
                            .name(NAME_2)
                            .eventType(TYPE_2)
                            .description(DESCRIPTION_2)
                            .content(CONTENT_2)
                            .durationInMinutes(DURATION_2)
                            .artist(ARTIST_DTO_2)
                            .build()
                    ),
                    PageRequest.of(0,10), 2));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByArtistID() {
        BDDMockito.
            given(eventRepository.findAllByArtist_Id(2L, PageRequest.of(0,10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Event.builder()
                        .id(ID_EVENT_2)
                        .name(NAME_2)
                        .eventType(TYPE_2)
                        .description(DESCRIPTION_2)
                        .content(CONTENT_2)
                        .durationInMinutes(DURATION_2)
                        .artist(ARTIST_2)
                        .build()
                ),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERE_ARTIST_ID)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        EventDTO.builder()
                            .id(ID_EVENT_2)
                            .name(NAME_2)
                            .eventType(TYPE_2)
                            .description(DESCRIPTION_2)
                            .content(CONTENT_2)
                            .durationInMinutes(DURATION_2)
                            .artist(ARTIST_DTO_2)
                            .build()
                    ),
                    PageRequest.of(0,10), 1));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByEventName() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setName("a").build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Event.builder()
                        .id(ID_EVENT_1)
                        .name(NAME_1)
                        .eventType(TYPE_1)
                        .description(DESCRIPTION_1)
                        .content(CONTENT_1)
                        .durationInMinutes(DURATION_1)
                        .artist(ARTIST_1)
                        .build()
                ),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_NAME)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        EventDTO.builder()
                            .id(ID_EVENT_1)
                            .name(NAME_1)
                            .eventType(TYPE_1)
                            .description(DESCRIPTION_1)
                            .content(CONTENT_1)
                            .durationInMinutes(DURATION_1)
                            .artist(ARTIST_DTO_1)
                            .build()
                    ),
                    PageRequest.of(0,10), 1));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByEventType() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setEventType(EventType.OPERA).build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Event.builder()
                        .id(ID_EVENT_2)
                        .name(NAME_2)
                        .eventType(TYPE_2)
                        .description(DESCRIPTION_2)
                        .content(CONTENT_2)
                        .durationInMinutes(DURATION_2)
                        .artist(ARTIST_2)
                        .build()
                ),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_TYPE)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        EventDTO.builder()
                            .id(ID_EVENT_2)
                            .name(NAME_2)
                            .eventType(TYPE_2)
                            .description(DESCRIPTION_2)
                            .content(CONTENT_2)
                            .durationInMinutes(DURATION_2)
                            .artist(ARTIST_DTO_2)
                            .build()
                    ),
                    PageRequest.of(0,10), 1));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByDescription() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setDescription("esc").build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Event.builder()
                        .id(ID_EVENT_1)
                        .name(NAME_1)
                        .eventType(TYPE_1)
                        .description(DESCRIPTION_1)
                        .content(CONTENT_1)
                        .durationInMinutes(DURATION_1)
                        .artist(ARTIST_1)
                        .build()
                ),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_DESCRIPTION)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        EventDTO.builder()
                            .id(ID_EVENT_1)
                            .name(NAME_1)
                            .eventType(TYPE_1)
                            .description(DESCRIPTION_1)
                            .content(CONTENT_1)
                            .durationInMinutes(DURATION_1)
                            .artist(ARTIST_DTO_1)
                            .build()
                    ),
                    PageRequest.of(0,10), 1));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByContent() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setContent("NO_CONTENT").build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(List.of(), PageRequest.of(0,10), 0));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_CONTENT)
            .then().extract().response();

        Assert.assertTrue(response.jsonPath().getList("content").isEmpty());
    }

    @Test
    public void findAllEventsFilteredByArtistName() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setArtistName("vasko").build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                Collections.singletonList(
                    Event.builder()
                        .id(ID_EVENT_1)
                        .name(NAME_1)
                        .eventType(TYPE_1)
                        .description(DESCRIPTION_1)
                        .content(CONTENT_1)
                        .durationInMinutes(DURATION_1)
                        .artist(ARTIST_1)
                        .build()
                ),
                PageRequest.of(0,10), 1)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_ARTIST_NAME)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Collections.singletonList(
                        EventDTO.builder()
                            .id(ID_EVENT_1)
                            .name(NAME_1)
                            .eventType(TYPE_1)
                            .description(DESCRIPTION_1)
                            .content(CONTENT_1)
                            .durationInMinutes(DURATION_1)
                            .artist(ARTIST_DTO_1)
                            .build()
                    ),
                    PageRequest.of(0,10), 1));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }

    @Test
    public void findAllEventsFilteredByDuration() {
        EventSearchParametersDTO parametersDTO = EventSearchParametersDTO.builder().setDurationInMinutes(200).build();
        BDDMockito.
            given(eventRepository.findAllEventsFiltered(parametersDTO,PageRequest.of(0, 10)))
            .willReturn(new PageImpl<>(
                List.of(
                    Event.builder()
                        .id(ID_EVENT_1)
                        .name(NAME_1)
                        .eventType(TYPE_1)
                        .description(DESCRIPTION_1)
                        .content(CONTENT_1)
                        .durationInMinutes(DURATION_1)
                        .artist(ARTIST_1)
                        .build(),
                    Event.builder()
                        .id(ID_EVENT_2)
                        .name(NAME_2)
                        .eventType(TYPE_2)
                        .description(DESCRIPTION_2)
                        .content(CONTENT_2)
                        .durationInMinutes(DURATION_2)
                        .artist(ARTIST_2)
                        .build()
                ),
                PageRequest.of(0,10), 2)
            );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_FILTERED_DURATION)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        try{
            String jsonObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                new PageImpl<>(
                    Arrays.asList(
                        EventDTO.builder()
                            .id(ID_EVENT_1)
                            .name(NAME_1)
                            .eventType(TYPE_1)
                            .description(DESCRIPTION_1)
                            .content(CONTENT_1)
                            .durationInMinutes(DURATION_1)
                            .artist(ARTIST_DTO_1)
                            .build(),
                        EventDTO.builder()
                            .id(ID_EVENT_2)
                            .name(NAME_2)
                            .eventType(TYPE_2)
                            .description(DESCRIPTION_2)
                            .content(CONTENT_2)
                            .durationInMinutes(DURATION_2)
                            .artist(ARTIST_DTO_2)
                            .build()
                    ),
                    PageRequest.of(0,10), 2));
            Assert.assertEquals(response.getBody().asString(), jsonObject);
        }catch (JsonProcessingException e) {
            Assert.fail();
        }
    }
}


