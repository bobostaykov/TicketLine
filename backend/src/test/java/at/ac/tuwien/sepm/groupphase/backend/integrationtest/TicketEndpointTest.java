package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketPostDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.xml.bind.SchemaOutputResolver;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

public class TicketEndpointTest extends BaseIntegrationTest {

    private static final String TICKET_ENDPOINT = "/tickets";
    private static final String RESERVED_TICKET = "/reserved";
    private static final String FIND_BY_NAME = "/name";
    private static final String PRINTABLE = "/printable";
    private static final String RECEIPT = "/receipt";
    private static final String TICKET = "/ticket";
    private static final String CANCELLATION = "/cancellation";
    private static final String BUY_TICKET = "/buy";
    private static final String SPECIFIC_TICKET_PATH = "/{id}";

    private static final long TEST_TICKET_ID = 1L;
    private static final TicketStatus TEST_TICKET_STATUS = TicketStatus.RESERVED;

    private static final String TEST_CUSTOMER_NAME = "TestName";
    private static final String TEST_CUSTOMER_FIRSTNAME = "TestFirstName";
    private static final String TEST_CUSTOMER_EMAIL = "testCustomer@test.com";
    private static final LocalDate TEST_CUSTOMER_BIRTHDATE = LocalDate.of(1996, 11, 13);
    private static final long TEST_CUSTOMER_ID = 1L;

    private static final String TEST_TICKET_ENDPOINT = "/tickets";
    private static final String TEST_TICKET_ENDPOINT_FILTER = TEST_TICKET_ENDPOINT + "/filter";
    private static final String TEST_SPECIFIC_TICKET_PATH = "/{ticketId}";

    private ArtistDTO TEST_ARTIST_DTO;
    private EventDTO TEST_EVENT_DTO;
    private List<EventDTO> TEST_EVENT_LIST_DTO;
    private ShowDTO TEST_SHOW_DTO;
    private List<ShowDTO> TEST_SHOW_LIST_DTO;
    private CustomerDTO TEST_CUSTOMER1_DTO;
    private CustomerDTO TEST_CUSTOMER2_DTO;
    private List<CustomerDTO> TEST_CUSTOMER1_LIST_DTO;
    private TicketDTO TEST_TICKET1_DTO;
    private TicketDTO TEST_TICKET2_DTO;
    private List<TicketDTO> TEST_TICKET_LIST_DTO;
    private List<TicketDTO> TEST_TICKET_LIST_BY_CUSTOMER_DTO;
    private List<TicketDTO> TEST_TICKET_LIST_BY_SHOW_DTO;
    private List<TicketDTO> TEST_TICKET_LIST_ALL_DTO;
    private HallDTO TEST_HALL_DTO;
    private LocationDTO TEST_LOCATION_DTO;
    private SeatDTO TEST_SEAT1_DTO;
    private SeatDTO TEST_SEAT2_DTO;
    private SeatDTO TEST_SEAT3_DTO;
    private Seat TEST_SEAT;
    private SectorDTO TEST_SECTOR_DTO;
    private Sector TEST_SECTOR;

    private Artist TEST_ARTIST;
    private Event TEST_EVENT;
    private List<Event> TEST_EVENT_LIST;
    private Show TEST_SHOW;
    private List<Show> TEST_SHOW_LIST;
    private Customer TEST_CUSTOMER1;
    private Customer TEST_CUSTOMER2;
    private List<Customer> TEST_CUSTOMER1_LIST;
    private Ticket TEST_TICKET1;
    private Ticket TEST_TICKET2;
    private List<Ticket> TEST_TICKET_LIST;
    private List<Ticket> TEST_TICKET_LIST_BY_CUSTOMER;
    private List<Ticket> TEST_TICKET_LIST_BY_SHOW;
    private List<Ticket> TEST_TICKET_LIST_ALL;
    private Hall TEST_HALL;
    private Location TEST_LOCATION;
    private Seat TEST_SEAT1;
    private Seat TEST_SEAT2;
    private Seat TEST_SEAT3;

    /******************************************************************
     TEST VARIABLES
     ******************************************************************/
    private Long TEST_ARTIST_ID = 2L;
    private String TEST_ARTIST_NAME = "artist_test_name";

    private Long TEST_EVENT_ID = 3L;
    private String TEST_EVENT_NAME = "event_test_name";
    private EventType TEST_EVENT_TYPE = EventType.OPERA;
    private String TEST_EVENT_DESCRIPTION = "event_test_descrption";
    private String TEST_EVENT_CONTENT = "event_test_content";
    private Integer TEST_EVENT_DURATION = 230;

    private Long TEST_LOCATION_ID = 4L;
    private String TEST_LOCATION_COUNTRY = "location_test_country";
    private String TEST_LOCATION_CITY = "location_test_city";
    private String TEST_LOCATION_POSTAL_CODE = "1180";
    private String TEST_LOCATION_STREET = "location_test_street";
    private String TEST_LOCATION_DESCRIPTION = "location_test_description";

    private Long TEST_SEAT_ID_1 = 51L;
    private Long TEST_SEAT_ID_2 = 52L;
    private Long TEST_SEAT_ID_3 = 53L;
    private Long TEST_SECTOR_ID = 54L;
    private Integer TEST_SEAT_SEAT_NO_1 = 43;
    private Integer TEST_SEAT_SEAT_NO_2 = 44;
    private Integer TEST_SEAT_SEAT_NO_3 = 45;
    private Integer TEST_SEAT_SEAT_ROW_1 = 25;
    private Integer TEST_SEAT_SEAT_ROW_2 = 25;
    private Integer TEST_SEAT_SEAT_ROW_3 = 25;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_1 = PriceCategory.CHEAP;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_2 = PriceCategory.AVERAGE;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_3 = PriceCategory.EXPENSIVE;

    private List<SeatDTO> TEST_HALL_SEATS_DTO;
    private List<Seat> TEST_HALL_SEATS;

    private Long TEST_HALL_ID = 6L;
    private String TEST_HALL_NAME = "hall_test_name";

    private Long TEST_SHOW_ID = 7L;
    private LocalTime TEST_SHOW_TIME = LocalTime.of(19,30,0,0);
    private LocalDate TEST_SHOW_DATE = LocalDate.of(2019,7,12);
    private String TEST_SHOW_DESCRIPTION = "show_test_description";
    private Long TEST_SHOW_TICKET_SOLD = 0L;

    private Long TEST_CUSTOMER_ID1 = 8L;
    private Long TEST_CUSTOMER_ID2 = 9L;
    private String TEST_CUSTOMER_NAME1 = "customer_test_name_1";
    private String TEST_CUSTOMER_NAME2 = "customer_test_name_2";
    private String TEST_CUSTOMER_FIRSTNAME1 = "customer_test_firstname_1";
    private String TEST_CUSTOMER_FIRSTNAME2 = "customer_test_firstname_1";
    private String TEST_CUSTOMER_EMAIL1 = "customer_test_1@email.com";
    private String TEST_CUSTOMER_EMAIL2 = "customer_test_2@email.com";
    private LocalDate TEST_CUSTOMER_BIRTHDAY1 = LocalDate.of(1975,8,14);
    private LocalDate TEST_CUSTOMER_BIRTHDAY2 = LocalDate.of(1964,1,4);

    private Long TEST_TICKET_ID1 = 11L;
    private Double TEST_TICKET_PRICE1 = 143.99;
    private Integer TEST_TICKET_SEAT_NO1 = 44;
    private Integer TEST_TICKET_ROW_NO1 = 25;
    private TicketStatus TEST_TICKET_STATUS1 = TicketStatus.SOLD;

    private Long TEST_TICKET_ID2 = 12L;
    private Double TEST_TICKET_PRICE2 = 15.50;
    private Integer TEST_SECTOR_NUMBER = 1;
    private TicketStatus TEST_TICKET_STATUS2 = TicketStatus.RESERVED;
    private String TEST_TICKET_RESERVATIONNO1 = "test_res_no_012345";

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ShowRepository showRepository;

    @MockBean
    private SeatRepository seatRepository;

    @MockBean
    private EventRepository eventRepository;

    @Before
    public void init() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Before
    public void before() {
        TEST_ARTIST_DTO = ArtistDTO.builder()
            .id(TEST_ARTIST_ID)
            .name(TEST_ARTIST_NAME)
            .build();
        TEST_EVENT_DTO = EventDTO.builder()
            .id(TEST_EVENT_ID)
            .name(TEST_EVENT_NAME)
            .eventType(TEST_EVENT_TYPE)
            .description(TEST_EVENT_DESCRIPTION)
            .content(TEST_EVENT_CONTENT)
            .durationInMinutes(TEST_EVENT_DURATION)
            .artist(TEST_ARTIST_DTO)
            .build();
        TEST_LOCATION_DTO = LocationDTO.builder()
            .id(TEST_LOCATION_ID)
            .country(TEST_LOCATION_COUNTRY)
            .city(TEST_LOCATION_CITY)
            .postalCode(TEST_LOCATION_POSTAL_CODE)
            .street(TEST_LOCATION_STREET)
            .description(TEST_LOCATION_DESCRIPTION)
            .build();
        TEST_HALL_SEATS_DTO = new ArrayList<>();
        TEST_SEAT1_DTO = SeatDTO.builder()
            .id(TEST_SEAT_ID_1)
            .seatNumber(TEST_SEAT_SEAT_NO_1)
            .seatRow(TEST_SEAT_SEAT_ROW_1)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_1)
            .build();
        TEST_SEAT2_DTO = SeatDTO.builder()
            .id(TEST_SEAT_ID_2)
            .seatNumber(TEST_SEAT_SEAT_NO_2)
            .seatRow(TEST_SEAT_SEAT_ROW_2)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_2)
            .build();
        TEST_SEAT3_DTO = SeatDTO.builder()
            .id(TEST_SEAT_ID_3)
            .seatNumber(TEST_SEAT_SEAT_NO_3)
            .seatRow(TEST_SEAT_SEAT_ROW_3)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_3)
            .build();
        TEST_SEAT = Seat.builder()
            .id(TEST_SEAT_ID_1)
            .seatNumber(TEST_SEAT_SEAT_NO_1)
            .seatRow(TEST_SEAT_SEAT_ROW_1)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_1)
            .build();
        TEST_SECTOR_DTO = SectorDTO.builder()
            .id(TEST_SECTOR_ID)
            .sectorNumber(TEST_SECTOR_NUMBER)
            .priceCategory(PriceCategory.AVERAGE)
            .build();
        TEST_SECTOR = Sector.builder()
            .id(TEST_SECTOR_ID)
            .sectorNumber(TEST_SECTOR_NUMBER)
            .priceCategory(PriceCategory.AVERAGE)
            .build();
        TEST_HALL_SEATS_DTO.add(TEST_SEAT1_DTO);
        TEST_HALL_SEATS_DTO.add(TEST_SEAT2_DTO);
        TEST_HALL_SEATS_DTO.add(TEST_SEAT3_DTO);
        TEST_HALL_DTO = HallDTO.builder()
            .id(TEST_HALL_ID)
            .name(TEST_HALL_NAME)
            .location(TEST_LOCATION_DTO)
            .seats(TEST_HALL_SEATS_DTO)
            .build();
        TEST_CUSTOMER1_DTO = CustomerDTO.builder()
            .id(TEST_CUSTOMER_ID1)
            .name(TEST_CUSTOMER_NAME1)
            .firstname(TEST_CUSTOMER_FIRSTNAME1)
            .email(TEST_CUSTOMER_EMAIL1)
            .birthday(TEST_CUSTOMER_BIRTHDAY1)
            .build();
        TEST_CUSTOMER2_DTO = CustomerDTO.builder()
            .id(TEST_CUSTOMER_ID2)
            .name(TEST_CUSTOMER_NAME2)
            .firstname(TEST_CUSTOMER_FIRSTNAME2)
            .email(TEST_CUSTOMER_EMAIL2)
            .birthday(TEST_CUSTOMER_BIRTHDAY2)
            .build();
        TEST_SHOW_DTO = ShowDTO.builder()
            .id(TEST_SHOW_ID)
            .event(TEST_EVENT_DTO)
            .hall(TEST_HALL_DTO)
            .time(TEST_SHOW_TIME)
            .date(TEST_SHOW_DATE)
            .description(TEST_SHOW_DESCRIPTION)
            .ticketsSold(TEST_SHOW_TICKET_SOLD)
            .build();
        TEST_TICKET1_DTO = TicketDTO.builder()
            .id(TEST_TICKET_ID1)
            .show(TEST_SHOW_DTO)
            .customer(TEST_CUSTOMER1_DTO)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT1_DTO)
            .status(TEST_TICKET_STATUS1)
            .build();
        TEST_TICKET2_DTO = TicketDTO.builder()
            .id(TEST_TICKET_ID2)
            .show(TEST_SHOW_DTO)
            .customer(TEST_CUSTOMER2_DTO)
            .price(TEST_TICKET_PRICE2)
            .sector(TEST_SECTOR_DTO)
            .status(TEST_TICKET_STATUS2)
            .build();
        TEST_CUSTOMER1_LIST_DTO = new ArrayList<>();
        TEST_CUSTOMER1_LIST_DTO.add(TEST_CUSTOMER1_DTO);
        TEST_EVENT_LIST_DTO = new ArrayList<>();
        TEST_EVENT_LIST_DTO.add(TEST_EVENT_DTO);
        TEST_SHOW_LIST_DTO = new ArrayList<>();
        TEST_SHOW_LIST_DTO.add(TEST_SHOW_DTO);
        TEST_TICKET_LIST_DTO = new ArrayList<>();
        TEST_TICKET_LIST_DTO.add(TEST_TICKET1_DTO);
        TEST_TICKET_LIST_BY_CUSTOMER_DTO = new ArrayList<>();
        TEST_TICKET_LIST_BY_CUSTOMER_DTO.add(TEST_TICKET1_DTO);
        TEST_TICKET_LIST_BY_SHOW_DTO = new ArrayList<>();
        TEST_TICKET_LIST_BY_SHOW_DTO.add(TEST_TICKET1_DTO);
        TEST_TICKET_LIST_BY_SHOW_DTO.add(TEST_TICKET2_DTO);
        TEST_TICKET_LIST_ALL_DTO = new ArrayList<>();
        TEST_TICKET_LIST_ALL_DTO.add(TEST_TICKET1_DTO);
        TEST_TICKET_LIST_ALL_DTO.add(TEST_TICKET2_DTO);

        TEST_ARTIST = Artist.builder()
            .id(TEST_ARTIST_ID)
            .name(TEST_ARTIST_NAME)
            .build();
        TEST_EVENT = Event.builder()
            .id(TEST_EVENT_ID)
            .name(TEST_EVENT_NAME)
            .eventType(TEST_EVENT_TYPE)
            .description(TEST_EVENT_DESCRIPTION)
            .content(TEST_EVENT_CONTENT)
            .durationInMinutes(TEST_EVENT_DURATION)
            .artist(TEST_ARTIST)
            .build();
        TEST_LOCATION = Location.builder()
            .id(TEST_LOCATION_ID)
            .country(TEST_LOCATION_COUNTRY)
            .city(TEST_LOCATION_CITY)
            .postalCode(TEST_LOCATION_POSTAL_CODE)
            .street(TEST_LOCATION_STREET)
            .description(TEST_LOCATION_DESCRIPTION)
            .build();
        TEST_HALL_SEATS = new ArrayList<Seat>();
        TEST_SEAT1 = Seat.builder()
            .id(TEST_SEAT_ID_1)
            .seatNumber(TEST_SEAT_SEAT_NO_1)
            .seatRow(TEST_SEAT_SEAT_ROW_1)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_1)
            .build();
        TEST_SEAT2 = Seat.builder()
            .id(TEST_SEAT_ID_2)
            .seatNumber(TEST_SEAT_SEAT_NO_2)
            .seatRow(TEST_SEAT_SEAT_ROW_2)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_2)
            .build();
        TEST_SEAT3 = Seat.builder()
            .id(TEST_SEAT_ID_3)
            .seatNumber(TEST_SEAT_SEAT_NO_3)
            .seatRow(TEST_SEAT_SEAT_ROW_3)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_3)
            .build();
        TEST_HALL_SEATS.add(TEST_SEAT1);
        TEST_HALL_SEATS.add(TEST_SEAT2);
        TEST_HALL_SEATS.add(TEST_SEAT3);
        TEST_HALL = Hall.builder()
            .id(TEST_HALL_ID)
            .name(TEST_HALL_NAME)
            .location(TEST_LOCATION)
            .seats(TEST_HALL_SEATS)
            .build();
        TEST_CUSTOMER1 = Customer.builder()
            .id(TEST_CUSTOMER_ID1)
            .name(TEST_CUSTOMER_NAME1)
            .firstname(TEST_CUSTOMER_FIRSTNAME1)
            .email(TEST_CUSTOMER_EMAIL1)
            .birthday(TEST_CUSTOMER_BIRTHDAY1)
            .build();
        TEST_CUSTOMER2 = Customer.builder()
            .id(TEST_CUSTOMER_ID2)
            .name(TEST_CUSTOMER_NAME2)
            .firstname(TEST_CUSTOMER_FIRSTNAME2)
            .email(TEST_CUSTOMER_EMAIL2)
            .birthday(TEST_CUSTOMER_BIRTHDAY2)
            .build();
        TEST_SHOW = Show.builder()
            .id(TEST_SHOW_ID)
            .event(TEST_EVENT)
            .hall(TEST_HALL)
            .time(TEST_SHOW_TIME)
            .date(TEST_SHOW_DATE)
            .description(TEST_SHOW_DESCRIPTION)
            .ticketsSold(TEST_SHOW_TICKET_SOLD)
            .build();
        TEST_TICKET1 = Ticket.builder()
            .id(TEST_TICKET_ID1)
            .show(TEST_SHOW)
            .customer(TEST_CUSTOMER1)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT1)
            .status(TEST_TICKET_STATUS1)
            .build();
        TEST_TICKET2 = Ticket.builder()
            .id(TEST_TICKET_ID2)
            .show(TEST_SHOW)
            .customer(TEST_CUSTOMER2)
            .price(TEST_TICKET_PRICE2)
            .sector(TEST_SECTOR)
            .status(TEST_TICKET_STATUS2)
            .build();
        TEST_CUSTOMER1_LIST = new ArrayList<>();
        TEST_CUSTOMER1_LIST.add(TEST_CUSTOMER1);
        TEST_EVENT_LIST = new ArrayList<>();
        TEST_EVENT_LIST.add(TEST_EVENT);
        TEST_SHOW_LIST = new ArrayList<>();
        TEST_SHOW_LIST.add(TEST_SHOW);
        TEST_TICKET_LIST = new ArrayList<>();
        TEST_TICKET_LIST.add(TEST_TICKET1);
        TEST_TICKET_LIST_BY_CUSTOMER = new ArrayList<>();
        TEST_TICKET_LIST_BY_CUSTOMER.add(TEST_TICKET1);
        TEST_TICKET_LIST_BY_SHOW = new ArrayList<>();
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET1);
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET2);
        TEST_TICKET_LIST_ALL = new ArrayList<>();
        TEST_TICKET_LIST_ALL.add(TEST_TICKET1);
        TEST_TICKET_LIST_ALL.add(TEST_TICKET2);
    }
/*
    @Test
    public void findAllTicketsAsUser() {
        BDDMockito.
            given(ticketRepository.findAllByOrderByIdAsc()).
            willReturn(Collections.singletonList(
                Ticket.builder()
                    .id(TEST_TICKET_ID1)
                    .show(TEST_SHOW)
                    .customer(TEST_CUSTOMER1)
                    .price(TEST_TICKET_PRICE1)
                    .seat(TEST_SEAT)
                    .status(TEST_TICKET_STATUS1)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TEST_TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID1)
                .show(TEST_SHOW_DTO)
                .customer(TEST_CUSTOMER1_DTO)
                .price(TEST_TICKET_PRICE1)
                .seat(TEST_SEAT1_DTO)
                .status(TEST_TICKET_STATUS1)
                .build())));
    }

 */



    @Test
    public void createTicketAsUser() {
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID1)
                .show(TEST_SHOW)
                .customer(TEST_CUSTOMER1)
                .price(TEST_TICKET_PRICE1)
                .seat(TEST_SEAT)
                .status(TEST_TICKET_STATUS1)
                .build());
        BDDMockito.
            given(customerRepository.getOne(TEST_CUSTOMER_ID1)).
            willReturn(TEST_CUSTOMER1);
        BDDMockito.
            given(showRepository.getOne(TEST_SHOW_ID)).
            willReturn(TEST_SHOW);
        BDDMockito.
            given(seatRepository.getOne(TEST_SEAT_ID_1)).
            willReturn(TEST_SEAT1);
        BDDMockito.
            given(showRepository.save(any(Show.class))).
            willReturn(TEST_SHOW);
        List<TicketPostDTO> body = new ArrayList<>();
        body.add(TicketPostDTO.builder()
            .show(TEST_SHOW_ID)
            .customer(TEST_CUSTOMER_ID1)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT_ID_1)
            .status(TEST_TICKET_STATUS1)
            .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(body)
            .when().post(TEST_TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(TicketDTO.builder()
            .id(TEST_TICKET_ID1)
            .show(TEST_SHOW_DTO)
            .customer(TEST_CUSTOMER1_DTO)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT1_DTO)
            .status(TEST_TICKET_STATUS1)
            .build())));
    }


    @Test
    public void deleteTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void deleteSpecificTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void deleteSpecificTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().delete(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .build()));
    }

    @Test
    public void findSpecificTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .customer(TEST_CUSTOMER1)
                .price(TEST_TICKET_PRICE1)
                .status(TEST_TICKET_STATUS1)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .customer(TEST_CUSTOMER1_DTO)
            .price(TEST_TICKET_PRICE1)
            .status(TEST_TICKET_STATUS1)
            .build()));
    }

    @Test
    public void findSpecificTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneById(TEST_TICKET_ID)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .customer(TEST_CUSTOMER1)
                .price(TEST_TICKET_PRICE1)
                .status(TEST_TICKET_STATUS1)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .customer(TEST_CUSTOMER1_DTO)
            .price(TEST_TICKET_PRICE1)
            .status(TEST_TICKET_STATUS1)
            .build()));
    }

    @Test
    public void findSpecificReservatedTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_ENDPOINT + RESERVED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificReservatedTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TEST_TICKET_STATUS)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + RESERVED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .status(TEST_TICKET_STATUS)
            .build()));
    }

    @Test
    public void findSpecificReservatedTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TEST_TICKET_STATUS)
                .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + RESERVED_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .status(TEST_TICKET_STATUS)
            .build()));
    }

    @Test
    public void buySpecificReservatedTicketUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void buySpecificReservatedTicketAsUser() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TicketStatus.SOLD)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .status(TicketStatus.SOLD)
            .build()));
    }

    @Test
    public void buySpecificReservatedTicketAsAdmin() {
        BDDMockito.
            given(ticketRepository.findOneByIdAndStatus(TEST_TICKET_ID, TEST_TICKET_STATUS)).
            willReturn(Optional.of(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID)
                .show(TEST_SHOW)
                .status(TicketStatus.SOLD)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().put(TICKET_ENDPOINT + BUY_TICKET + SPECIFIC_TICKET_PATH, TEST_TICKET_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(TicketDTO.class), is(TicketDTO.builder()
            .id(TEST_TICKET_ID)
            .show(TEST_SHOW_DTO)
            .status(TicketStatus.SOLD)
            .build()));
    }

    @Test
    public void getReceiptForListOfTickets() {
        List<Long> ticketIDs = new ArrayList<>();
        ticketIDs.add(TEST_TICKET_ID1);
        ticketIDs.add(TEST_TICKET_ID2);
        List<Ticket> returnedTickets = new ArrayList<>();
        returnedTickets.add(Ticket.builder().id(TEST_TICKET_ID1).seat(TEST_SEAT).price(TEST_TICKET_PRICE1).customer(TEST_CUSTOMER1).show(TEST_SHOW).status(TEST_TICKET_STATUS1).build());
        returnedTickets.add(Ticket.builder().id(TEST_TICKET_ID2).seat(TEST_SEAT2).price(TEST_TICKET_PRICE2).customer(TEST_CUSTOMER2).show(TEST_SHOW).status(TEST_TICKET_STATUS2).build());
        BDDMockito.
            given(ticketRepository.findByIdIn(ticketIDs)).
            willReturn(returnedTickets);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .params("tickets", TEST_TICKET_ID1)
            .params("tickets", TEST_TICKET_ID2)
            .when().get(TICKET_ENDPOINT + PRINTABLE + RECEIPT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.contentType(), is("application/pdf" ));
    }

    @Test
    public void getReceiptForEmptyListOfTickets() {
        BDDMockito.
            given(ticketRepository.findByIdIn(Collections.EMPTY_LIST)).
            willReturn(Collections.EMPTY_LIST);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_ENDPOINT + PRINTABLE + RECEIPT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void deleteTicketsAndReceiveReceipt() {
        List<Long> ticketIDs = new ArrayList<>();
        ticketIDs.add(TEST_TICKET_ID1);
        ticketIDs.add(TEST_TICKET_ID2);
        List<Ticket> returnedTickets = new ArrayList<>();
        returnedTickets.add(Ticket.builder().id(TEST_TICKET_ID1).seat(TEST_SEAT).price(TEST_TICKET_PRICE1).customer(TEST_CUSTOMER1).show(TEST_SHOW).status(TEST_TICKET_STATUS1).build());
        returnedTickets.add(Ticket.builder().id(TEST_TICKET_ID2).seat(TEST_SEAT2).price(TEST_TICKET_PRICE2).customer(TEST_CUSTOMER2).show(TEST_SHOW).status(TEST_TICKET_STATUS2).build());
        BDDMockito.
            given(ticketRepository.findByIdIn(ticketIDs)).
            willReturn(returnedTickets);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .params("tickets", TEST_TICKET_ID1)
            .params("tickets", TEST_TICKET_ID2)
            .when().delete(TICKET_ENDPOINT + PRINTABLE + CANCELLATION)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.contentType(), is("application/pdf" ));
    }

    @Test
    public void postTicktetThatAlreadyExistsExpectingTicketSoldOutException() {
        BDDMockito.
            given(ticketRepository.save(any(Ticket.class))).
            willReturn(Ticket.builder()
                .id(TEST_TICKET_ID1)
                .show(TEST_SHOW)
                .customer(TEST_CUSTOMER1)
                .price(TEST_TICKET_PRICE1)
                .seat(TEST_SEAT)
                .status(TEST_TICKET_STATUS1)
                .build());
        BDDMockito.
            given(customerRepository.getOne(TEST_CUSTOMER_ID1)).
            willReturn(TEST_CUSTOMER1);
        BDDMockito.
            given(showRepository.getOne(TEST_SHOW_ID)).
            willReturn(TEST_SHOW);
        BDDMockito.
            given(seatRepository.getOne(TEST_SEAT_ID_1)).
            willReturn(TEST_SEAT1);
        BDDMockito.
            given(showRepository.save(any(Show.class))).
            willReturn(TEST_SHOW);
        BDDMockito.
            given(ticketRepository.findAllByShowAndSeat(any(Show.class), any(Seat.class))).
            willReturn(TEST_TICKET_LIST);
        List<TicketPostDTO> body = new ArrayList<>();
        body.add(TicketPostDTO.builder()
            .show(TEST_SHOW_ID)
            .customer(TEST_CUSTOMER_ID1)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT_ID_1)
            .status(TEST_TICKET_STATUS1)
            .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(body)
            .when().post(TEST_TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
        Assert.assertEquals(response.asString(), "{\n" +
            "  \"status\" : \"BAD_REQUEST\",\n" +
            "  \"message\" : \"400 BAD_REQUEST \\\"Ticket for this seat is already sold, please choose another seat\\\"\",\n" +
            "  \"errors\" : [ \"400 BAD_REQUEST \\\"Ticket for this seat is already sold, please choose another seat\\\"\" ]\n" +
            "}");
    }

    @Test
    public void testFindTicketFilteredByCustomerAndEventSuccessfull() {
        BDDMockito.given(customerRepository.
            findAllByNameContainsIgnoreCase("est")).
            willReturn(TEST_CUSTOMER1_LIST);
        BDDMockito.given(eventRepository.
            findAllByNameContainsIgnoreCase("ent_")).
            willReturn(TEST_EVENT_LIST);
        BDDMockito.given(showRepository.
            findAllByEventIn(TEST_EVENT_LIST)).
            willReturn(TEST_SHOW_LIST);
        BDDMockito.given(ticketRepository.
            findAllByCustomerIn(TEST_CUSTOMER1_LIST)).
            willReturn(TEST_TICKET_LIST);
        BDDMockito.given(ticketRepository.
            findAllByShowIn(TEST_SHOW_LIST)).
            willReturn(TEST_TICKET_LIST);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TEST_TICKET_ENDPOINT_FILTER + "?customerName=est&eventName=ent_&page=0")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        List<TicketDTO> tickets = response.jsonPath().getList("content");
        Assert.assertTrue(tickets.size() == 1);
    }
/*
    @Test
    public void testFindAlTicketsFilteredWithoutFilterParamsSuccessfull() {
        BDDMockito.
            given(ticketRepository.findAllByOrderByIdAsc()).
            willReturn(Collections.singletonList(
                Ticket.builder()
                    .id(TEST_TICKET_ID1)
                    .show(TEST_SHOW)
                    .customer(TEST_CUSTOMER1)
                    .price(TEST_TICKET_PRICE1)
                    .seat(TEST_SEAT)
                    .status(TEST_TICKET_STATUS1)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TEST_TICKET_ENDPOINT_FILTER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID1)
                .show(TEST_SHOW_DTO)
                .customer(TEST_CUSTOMER1_DTO)
                .price(TEST_TICKET_PRICE1)
                .seat(TEST_SEAT1_DTO)
                .status(TEST_TICKET_STATUS1)
                .build())));
    }

 */



    // TESTS FOR PINOS IMPLEMENTATION
    /*
    @Test
    public void findReservatedTicketsByCustomerUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(ShowDTO.builder()
                .id(TEST_SHOW_ID)
                .build())
            .param("customerName", TEST_CUSTOMER_NAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findReservatedTicketsByCustomerNameAndShowAsUser() {
        BDDMockito.
            given(ticketRepository.findAllByCustomerAndShowWithStatusReservated(any(Customer.class), any(Show.class))).
            willReturn(Collections.singletonList(
                Ticket.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build()));
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, TEST_CUSTOMER_NAME, TEST_CUSTOMER_FIRSTNAME, null, null)).
            willReturn(Collections.singletonList(
                Customer.builder()
                    .id(TEST_CUSTOMER_ID)
                    .name(TEST_CUSTOMER_NAME)
                    .firstname(TEST_CUSTOMER_FIRSTNAME)
                    .email(TEST_CUSTOMER_EMAIL)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ShowDTO.builder()
            .id(TEST_SHOW_ID)
            .build())
            .param("surname", TEST_CUSTOMER_NAME)
            .param("firstname", TEST_CUSTOMER_FIRSTNAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build())));
    }

    @Test
    public void findReservatedTicketsByCustomerNameAndShowAsAdmin() {
        BDDMockito.
            given(ticketRepository.findAllByCustomerAndShowWithStatusReservated(any(Customer.class), any(Show.class))).
            willReturn(Collections.singletonList(
                Ticket.builder()
                    .id(TEST_TICKET_ID)
                    .status(TEST_TICKET_STATUS)
                    .build()));
        BDDMockito.
            given(customerRepository.findCustomersFiltered(null, TEST_CUSTOMER_NAME, TEST_CUSTOMER_FIRSTNAME, null, null)).
            willReturn(Collections.singletonList(
                Customer.builder()
                    .id(TEST_CUSTOMER_ID)
                    .name(TEST_CUSTOMER_NAME)
                    .firstname(TEST_CUSTOMER_FIRSTNAME)
                    .email(TEST_CUSTOMER_EMAIL)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(ShowDTO.builder()
                .id(TEST_SHOW_ID)
                .build())
            .param("surname", TEST_CUSTOMER_NAME)
            .param("firstname", TEST_CUSTOMER_FIRSTNAME)
            .when().get(TICKET_ENDPOINT + FIND_BY_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(TicketDTO[].class)), is(Collections.singletonList(
            TicketDTO.builder()
                .id(TEST_TICKET_ID)
                .status(TEST_TICKET_STATUS)
                .build())));
    }
     */

}
