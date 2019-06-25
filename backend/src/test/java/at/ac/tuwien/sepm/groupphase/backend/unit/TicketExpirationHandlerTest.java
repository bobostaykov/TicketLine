package at.ac.tuwien.sepm.groupphase.backend.unit;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandler;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandlerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketExpirationHandlerTest {
    private TicketRepository ticketRepository;

    private TicketExpirationHandler ticketExpirationHandler;
    private TicketMapper ticketMapper = new TicketMapperImpl();
    private ShowMapper showMapper = new ShowMapperImpl();
    private ShowRepository showRepository;

    private Artist TEST_ARTIST;
    private Event TEST_EVENT;
    private List<Event> TEST_EVENT_LIST;
    private Show TEST_SHOW_EXPIRED;
    private Show TEST_SHOW_NOT_EXPIRED;
    private List<Show> TEST_SHOW_LIST;
    private Customer TEST_CUSTOMER1;
    private Customer TEST_CUSTOMER2;
    private List<Customer> TEST_CUSTOMER1_LIST;
    private Ticket TEST_TICKET_EXPIRED;
    private Ticket TEST_TICKET_NOT_EXPIRED;
    private List<Ticket> TEST_TICKET_LIST;
    private List<TicketDTO> TEST_TICKET_LIST_DTO;
    private List<Ticket> TEST_TICKET_LIST_BY_CUSTOMER;
    private List<Ticket> TEST_TICKET_LIST_BY_SHOW;
    private Hall TEST_HALL;
    private Location TEST_LOCATION;
    private Seat TEST_SEAT1;
    private Seat TEST_SEAT2;
    private Seat TEST_SEAT3;
    private Sector TEST_SECTOR;

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
    private Long TEST_SECTOR_ID = 55L;
    private Integer TEST_SECTOR_NUMBER = 5;
    private Integer TEST_SEAT_SEAT_NO_1 = 43;
    private Integer TEST_SEAT_SEAT_NO_2 = 44;
    private Integer TEST_SEAT_SEAT_NO_3 = 45;
    private Integer TEST_SEAT_SEAT_ROW_1 = 25;
    private Integer TEST_SEAT_SEAT_ROW_2 = 25;
    private Integer TEST_SEAT_SEAT_ROW_3 = 25;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_1 = PriceCategory.CHEAP;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_2 = PriceCategory.AVERAGE;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_3 = PriceCategory.EXPENSIVE;

    private List<Seat> TEST_HALL_SEATS;

    private Long TEST_HALL_ID = 6L;
    private String TEST_HALL_NAME = "hall_test_name";

    private Long TEST_SHOW_ID_1 = 7L;
    private LocalTime TEST_SHOW_TIME_1 = LocalTime.now().plusMinutes(25);
    private LocalDate TEST_SHOW_DATE_1 = LocalDate.now();
    private String TEST_SHOW_DESCRIPTION_1 = "show_test_description";
    private Long TEST_SHOW_TICKET_SOLD_1 = 0L;

    private Long TEST_SHOW_ID_2 = 8L;
    private LocalTime TEST_SHOW_TIME_2 = LocalTime.now().plusMinutes(40);
    private LocalDate TEST_SHOW_DATE_2 = LocalDate.now();
    private String TEST_SHOW_DESCRIPTION_2 = "show_test_description";
    private Long TEST_SHOW_TICKET_SOLD_2 = 0L;

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
    private TicketStatus TEST_TICKET_STATUS1 = TicketStatus.RESERVED;

    private Long TEST_TICKET_ID2 = 12L;
    private Double TEST_TICKET_PRICE2 = 15.50;
    private Integer TEST_TICKET_SECTOR2 = 1;
    private TicketStatus TEST_TICKET_STATUS2 = TicketStatus.RESERVED;

    @Before
    public void before() {
        this.ticketRepository = Mockito.mock(TicketRepository.class);
        this.showRepository = Mockito.mock(ShowRepository.class);

        this.ticketExpirationHandler = new TicketExpirationHandlerImpl(this.ticketRepository, this.ticketMapper, this.showMapper, this.showRepository);

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
        TEST_SECTOR = Sector.builder()
            .id(TEST_SECTOR_ID)
            .sectorNumber(TEST_SECTOR_NUMBER)
            .priceCategory(PriceCategory.AVERAGE)
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
        TEST_SHOW_EXPIRED = Show.builder()
            .id(TEST_SHOW_ID_1)
            .event(TEST_EVENT)
            .hall(TEST_HALL)
            .time(TEST_SHOW_TIME_1)
            .date(TEST_SHOW_DATE_1)
            .description(TEST_SHOW_DESCRIPTION_1)
            .ticketsSold(TEST_SHOW_TICKET_SOLD_1)
            .build();
        TEST_SHOW_NOT_EXPIRED = Show.builder()
            .id(TEST_SHOW_ID_2)
            .event(TEST_EVENT)
            .hall(TEST_HALL)
            .time(TEST_SHOW_TIME_2)
            .date(TEST_SHOW_DATE_2)
            .description(TEST_SHOW_DESCRIPTION_2)
            .ticketsSold(TEST_SHOW_TICKET_SOLD_2)
            .build();
        TEST_TICKET_EXPIRED = Ticket.builder()
            .id(TEST_TICKET_ID1)
            .show(TEST_SHOW_EXPIRED)
            .customer(TEST_CUSTOMER1)
            .price(TEST_TICKET_PRICE1)
            .seat(TEST_SEAT1)
            .status(TEST_TICKET_STATUS1)
            .build();
        TEST_TICKET_NOT_EXPIRED = Ticket.builder()
            .id(TEST_TICKET_ID2)
            .show(TEST_SHOW_NOT_EXPIRED)
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
        TEST_SHOW_LIST.add(TEST_SHOW_EXPIRED);
        TEST_SHOW_LIST.add(TEST_SHOW_NOT_EXPIRED);
        TEST_TICKET_LIST = new ArrayList<>();
        TEST_TICKET_LIST.add(TEST_TICKET_EXPIRED);
        TEST_TICKET_LIST.add(TEST_TICKET_NOT_EXPIRED);
        TEST_TICKET_LIST_DTO = new ArrayList<>();
        TEST_TICKET_LIST_DTO.add(ticketMapper.ticketToTicketDTO(TEST_TICKET_EXPIRED));
        TEST_TICKET_LIST_BY_CUSTOMER = new ArrayList<>();
        TEST_TICKET_LIST_BY_CUSTOMER.add(TEST_TICKET_EXPIRED);
        TEST_TICKET_LIST_BY_SHOW = new ArrayList<>();
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET_EXPIRED);
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET_NOT_EXPIRED);
    }

    @Test
    public void setSpecificExpiredReservatedTicketToExpired() {
        Mockito.when(ticketRepository.findAllByShowAndStatus(TEST_SHOW_EXPIRED, TicketStatus.RESERVED)).thenReturn(Collections.singletonList(TEST_TICKET_NOT_EXPIRED));
        Mockito.when(showRepository.findAll()).thenReturn(TEST_SHOW_LIST);
        TicketDTO result = ticketExpirationHandler.setExpiredReservedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(TEST_TICKET_EXPIRED));
        Assert.assertEquals(result.getStatus(), TicketStatus.EXPIRED);
    }

    @Test
    public void setSpecificNotExpiredReservatedTicketNotToExpired() {
        Mockito.when(ticketRepository.findAllByShowAndStatus(TEST_SHOW_EXPIRED, TicketStatus.RESERVED)).thenReturn(Collections.singletonList(TEST_TICKET_NOT_EXPIRED));
        Mockito.when(showRepository.findAll()).thenReturn(TEST_SHOW_LIST);
        TicketDTO result = ticketExpirationHandler.setExpiredReservedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(TEST_TICKET_NOT_EXPIRED));
        Assert.assertEquals(result.getStatus(), TEST_TICKET_NOT_EXPIRED.getStatus());
    }

    @Test
    public void setListOfExpiredReservatedTicketToExpired() {
        Mockito.when(ticketRepository.findAllByShowAndStatus(TEST_SHOW_EXPIRED, TicketStatus.RESERVED)).thenReturn(Collections.singletonList(TEST_TICKET_NOT_EXPIRED));
        Mockito.when(showRepository.findAll()).thenReturn(TEST_SHOW_LIST);
        List<TicketDTO> result = ticketExpirationHandler.setExpiredReservedTicketsToStatusExpired(ticketMapper.ticketToTicketDTO(TEST_TICKET_LIST));
        int countExpiredTickets = 0;
        for (TicketDTO t: result) {
            if(t.getStatus()==TicketStatus.EXPIRED)
                countExpiredTickets++;
        }
        Assert.assertEquals(countExpiredTickets, 1);
    }
}