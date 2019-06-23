package at.ac.tuwien.sepm.groupphase.backend.unit;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketPostDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.TicketSoldOutException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.implementation.TicketServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandler;
import at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler.TicketExpirationHandlerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TicketServiceTest {
    private CustomerRepository customerRepository;
    private EventRepository eventRepository;
    private ShowRepository showRepository;
    private TicketRepository ticketRepository;
    private ShowMapper showMapper;
    private TicketExpirationHandler ticketExpirationHandler;
    private SeatRepository seatRepository;
    private SectorRepository sectorRepository;

    private TicketServiceImpl ticketService;
    private TicketMapper ticketMapper = new TicketMapperImpl();

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
    private Ticket TEST_POST_TICKET1;
    private Ticket TEST_POST_TICKET2;
    private List<Ticket> TEST_TICKET_LIST;
    private List<TicketPostDTO> TEST_TICKET_SEATS_POST_DTO_LIST;
    private List<TicketPostDTO> TEST_TICKET_SEATS_POST_DTO_LIST2;
    private List<TicketPostDTO> TEST_TICKET_POST_DTO_LIST;
    private List<TicketPostDTO> TEST_TICKET_SECTOR_POST_DTO_LIST;
    private List<TicketDTO> TEST_TICKET_LIST_DTO;
    private List<Ticket> TEST_TICKET_LIST_BY_CUSTOMER;
    private List<Ticket> TEST_TICKET_LIST_BY_SHOW;
    private Hall TEST_HALL;
    private Location TEST_LOCATION;
    private Seat TEST_SEAT1;
    private Seat TEST_SEAT2;
    private Seat TEST_SEAT3;
    private Seat TEST_SEAT4;
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
    private Long TEST_SEAT_ID_4 = 54L;
    private Long TEST_SECTOR_ID = 55L;
    private Integer TEST_SECTOR_NUMBER = 5;
    private Integer TEST_SEAT_SEAT_NO_1 = 43;
    private Integer TEST_SEAT_SEAT_NO_2 = 44;
    private Integer TEST_SEAT_SEAT_NO_3 = 45;
    private Integer TEST_SEAT_SEAT_NO_4 = 46;
    private Integer TEST_SEAT_SEAT_ROW_1 = 25;
    private Integer TEST_SEAT_SEAT_ROW_2 = 25;
    private Integer TEST_SEAT_SEAT_ROW_3 = 25;
    private Integer TEST_SEAT_SEAT_ROW_4 = 26;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_1 = PriceCategory.CHEAP;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_2 = PriceCategory.AVERAGE;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_3 = PriceCategory.EXPENSIVE;
    private PriceCategory TEST_SEAT_PRICE_CATEGORY_4 = PriceCategory.AVERAGE;

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
    private Integer TEST_TICKET_SECTOR2 = 1;
    private TicketStatus TEST_TICKET_STATUS2 = TicketStatus.RESERVATED;

    @Before
    public void before() {
        this.customerRepository = Mockito.mock(CustomerRepository.class);
        this.eventRepository = Mockito.mock(EventRepository.class);
        this.showRepository = Mockito.mock(ShowRepository.class);
        this.ticketRepository = Mockito.mock(TicketRepository.class);
        this.ticketExpirationHandler = Mockito.mock(TicketExpirationHandler.class);

        this.showMapper = new ShowMapperImpl();
        this.seatRepository = Mockito.mock(SeatRepository.class);
        this.sectorRepository = Mockito.mock(SectorRepository.class);

        this.ticketService = new TicketServiceImpl(this.ticketRepository, this.customerRepository, this.eventRepository,
            this.ticketMapper, this.showRepository, this.seatRepository, this.sectorRepository,  null,
            this.ticketExpirationHandler, showMapper);

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
        TEST_SEAT4 = Seat.builder()
            .id(TEST_SEAT_ID_4)
            .seatNumber(TEST_SEAT_SEAT_NO_4)
            .seatRow(TEST_SEAT_SEAT_ROW_4)
            .priceCategory(TEST_SEAT_PRICE_CATEGORY_4)
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
        TEST_TICKET_LIST.add(TEST_TICKET2);
        TEST_TICKET_LIST.add(TEST_TICKET2);
        TEST_TICKET_LIST_DTO = new ArrayList<>();
        TEST_TICKET_LIST_DTO.add(ticketMapper.ticketToTicketDTO(TEST_TICKET1));
        TEST_TICKET_SEATS_POST_DTO_LIST =  new ArrayList<>();
        TEST_TICKET_SEATS_POST_DTO_LIST.add(new TicketPostDTO().builder()
            .price(TEST_TICKET_PRICE1)
            .customer(TEST_CUSTOMER_ID1)
            .seat(TEST_SEAT_ID_1)
            .show(TEST_SHOW_ID)
            .status(TicketStatus.SOLD)
            .build());
        TEST_TICKET_SEATS_POST_DTO_LIST.add(new TicketPostDTO().builder()
            .price(TEST_TICKET_PRICE2)
            .customer(TEST_CUSTOMER_ID2)
            .seat(TEST_SEAT_ID_2)
            .show(TEST_SHOW_ID)
            .status(TicketStatus.SOLD)
            .build());
        TEST_TICKET_SEATS_POST_DTO_LIST2 =  new ArrayList<>();
        TEST_TICKET_SEATS_POST_DTO_LIST2.add(new TicketPostDTO().builder()
            .price(TEST_TICKET_PRICE1)
            .customer(TEST_CUSTOMER_ID1)
            .seat(TEST_SEAT_ID_4)
            .show(TEST_SHOW_ID)
            .status(TicketStatus.SOLD)
            .build());
        TEST_TICKET_POST_DTO_LIST =  new ArrayList<>();
        TEST_TICKET_POST_DTO_LIST.add(new TicketPostDTO().builder()
            .price(TEST_TICKET_PRICE1)
            .customer(TEST_CUSTOMER_ID1)
            .show(TEST_SHOW_ID)
            .status(TicketStatus.SOLD)
            .build());
        TEST_TICKET_SECTOR_POST_DTO_LIST =  new ArrayList<>();
        TEST_TICKET_SECTOR_POST_DTO_LIST.add(new TicketPostDTO().builder()
            .price(TEST_TICKET_PRICE1)
            .customer(TEST_CUSTOMER_ID1)
            .sector(TEST_SECTOR_ID)
            .show(TEST_SHOW_ID)
            .status(TicketStatus.SOLD)
            .build());
        TEST_TICKET_LIST_BY_CUSTOMER = new ArrayList<>();
        TEST_TICKET_LIST_BY_CUSTOMER.add(TEST_TICKET1);
        TEST_TICKET_LIST_BY_SHOW = new ArrayList<>();
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET1);
        TEST_TICKET_LIST_BY_SHOW.add(TEST_TICKET2);
    }

    @Test
    public void testFindAllFilteredByCustomerAndEvent_Successfull() {
        Mockito.when(customerRepository.findAllByName(TEST_CUSTOMER_NAME1)).thenReturn(TEST_CUSTOMER1_LIST);
        Mockito.when(eventRepository.findAllByName(TEST_EVENT_NAME)).thenReturn(TEST_EVENT_LIST);
        Mockito.when(showRepository.findAllByEvent(TEST_EVENT_LIST)).thenReturn(TEST_SHOW_LIST);
        Mockito.when(ticketRepository.findAllByCustomer(TEST_CUSTOMER1_LIST)).thenReturn(TEST_TICKET_LIST_BY_CUSTOMER);
        Mockito.when(ticketRepository.findAllByShow(TEST_SHOW_LIST)).thenReturn(TEST_TICKET_LIST_BY_SHOW);
        Mockito.when(ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(TEST_TICKET_LIST_DTO)).thenReturn(TEST_TICKET_LIST_DTO);
        //BDDMockito.given(ticketExpirationHandler.setExpiredReservatedTicketsToStatusExpired(any(ShowDTO.class));
        List<TicketDTO> result = ticketService.findAllFilteredByCustomerAndEvent(TEST_CUSTOMER_NAME1, TEST_EVENT_NAME);
        assertEquals(result, TEST_TICKET_LIST_DTO);
    }

    @Test
    public void testPostTicketsWithSeatsWhenTicketAlreadyExistsExpectingTicketSoldOutException() {
        Mockito.when(ticketRepository.findAllByShowAndSeat(TEST_SHOW, TEST_SEAT1)).thenReturn(TEST_TICKET_LIST);
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(TEST_SHOW);
        Mockito.when(seatRepository.getOne(TEST_SEAT_ID_1)).thenReturn(TEST_SEAT1);
        try{
            ticketService.postTicket(TEST_TICKET_SEATS_POST_DTO_LIST);
            fail("No TicketSoldOutException was thrown!");
        }catch (TicketSoldOutException e) {
            assertEquals(e.getClass(), TicketSoldOutException.class);
            assertEquals(e.getMessage(), "Ticket for this seat is already sold, please choose another seat");
        }
    }

    @Test
    public void testPostTicketsWithSectorsWhenTicketAlreadyExistsExpectingTicketSoldOutException() {
        Mockito.when(ticketRepository.findAllByShowAndSector(TEST_SHOW, TEST_SECTOR)).thenReturn(TEST_TICKET_LIST);
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(TEST_SHOW);
        Mockito.when(sectorRepository.getOne(TEST_SECTOR_ID)).thenReturn(TEST_SECTOR);
        try{
            ticketService.postTicket(TEST_TICKET_SECTOR_POST_DTO_LIST);
            fail("No TicketSoldOutException was thrown!");
        }catch (TicketSoldOutException e) {
            assertEquals(e.getClass(), TicketSoldOutException.class);
            assertEquals(e.getMessage(), "Tickets for this sector are sold out, please choose another sector");
        }
    }

    @Test
    public void testPostTicketSubmittingNonExistingCustomerExpectingNotFoundException() {
        Mockito.when(ticketRepository.findAllByShowAndSeat(TEST_SHOW, TEST_SEAT1)).thenReturn(new ArrayList<>());
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(TEST_SHOW);
        Mockito.when(seatRepository.getOne(TEST_SEAT_ID_1)).thenReturn(TEST_SEAT1);
        Mockito.when(customerRepository.getOne(TEST_CUSTOMER_ID1)).thenReturn(null);
        try{
            ticketService.postTicket(TEST_TICKET_SEATS_POST_DTO_LIST);
            fail("No NotFoundException was thrown!");
        }catch (NotFoundException e) {
            assertEquals(e.getClass(), NotFoundException.class);
            assertEquals(e.getMessage(), "No Customer found with id " + TEST_CUSTOMER_ID1);
        }catch (TicketSoldOutException e) {
            fail("Wrong exception '" + e.getClass().toString() + "' was thrown, expected NotFoundException");
        }
    }

    @Test
    public void testPostTicketSubmittingNonExistingShowExpectingNotFoundException() {
        Mockito.when(ticketRepository.findAllByShowAndSeat(TEST_SHOW, TEST_SEAT1)).thenReturn(new ArrayList<>());
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(TEST_SHOW);
        Mockito.when(seatRepository.getOne(TEST_SEAT_ID_1)).thenReturn(TEST_SEAT1);
        Mockito.when(customerRepository.getOne(TEST_CUSTOMER_ID1)).thenReturn(TEST_CUSTOMER1);
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(null);
        try{
            ticketService.postTicket(TEST_TICKET_SEATS_POST_DTO_LIST);
            fail("No NotFoundException was thrown!");
        }catch (NotFoundException e) {
            assertEquals(e.getClass(), NotFoundException.class);
            assertEquals(e.getMessage(), "No Show found with id " + TEST_SHOW_ID);
        }catch (TicketSoldOutException e) {
            fail("Wrong exception '" + e.getClass().toString() + "' was thrown, expected NotFoundException");
        }
    }

    @Test
    public void testPostTicketSubmittingWrongSeatForGivenShowExpectingNotFoundException() {
        Mockito.when(ticketRepository.findAllByShowAndSeat(TEST_SHOW, TEST_SEAT1)).thenReturn(new ArrayList<>());
        Mockito.when(showRepository.getOne(TEST_SHOW_ID)).thenReturn(TEST_SHOW);
        Mockito.when(seatRepository.getOne(TEST_SEAT_ID_1)).thenReturn(TEST_SEAT1);
        Mockito.when(seatRepository.getOne(TEST_SEAT_ID_4)).thenReturn(TEST_SEAT4);
        Mockito.when(customerRepository.getOne(TEST_CUSTOMER_ID1)).thenReturn(TEST_CUSTOMER1);
        try{
            ticketService.postTicket(TEST_TICKET_SEATS_POST_DTO_LIST2);
            fail("No NotFoundException was thrown!");
        }catch (NotFoundException e) {
            assertEquals(e.getClass(), NotFoundException.class);
            assertEquals(e.getMessage(), "Seat 46 in row 26 not found in list of seats for this show!");
        }catch (TicketSoldOutException e) {
            fail("Wrong exception '" + e.getClass().toString() + "' was thrown, expected NotFoundException");
        }
    }

    @Test
    public void testPostTicketSubmittingNoSeatOrSectorExpectingNotFoundException() {
        Mockito.when(customerRepository.getOne(TEST_CUSTOMER_ID1)).thenReturn(TEST_CUSTOMER1);
        try{
            ticketService.postTicket(TEST_TICKET_POST_DTO_LIST);
            fail("No NotFoundException was thrown!");
        }catch (NotFoundException e) {
            assertEquals(e.getClass(), NotFoundException.class);
            assertEquals(e.getMessage(), "Either seat or sector must be given");
        }catch (TicketSoldOutException e) {
            fail("Wrong exception '" + e.getClass().toString() + "' was thrown, expected NotFoundException");
        }
    }

}