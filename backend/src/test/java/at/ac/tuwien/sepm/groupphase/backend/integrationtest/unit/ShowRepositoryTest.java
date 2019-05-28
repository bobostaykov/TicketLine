package at.ac.tuwien.sepm.groupphase.backend.integrationtest.unit;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShowRepositoryTest {

    @Autowired
    private  ShowRepository showRepository;
    @Autowired
    private  ArtistRepository artistRepository;
    @Autowired
    private  EventRepository eventRepository;
    @Autowired
    private  HallRepository hallRepository;
    @Autowired
    private  LocationRepository locationRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");




    private Location locationAustria = Location.builder().id(1L).country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse ").houseNr(66).locationName("Stadthalle").build();
    private Location locationGermany = Location.builder().country("Germany").city("Bonn").postalCode("53129").street("Ermekeilstrasse").houseNr(17).locationName("chez Horst").build();
    private Hall hall1 = Hall.builder().id(1L).name("TopLocation").location(locationAustria).build();
    private Hall hall2 = Hall.builder().id(2L).name("Konzertsaal").location(locationGermany).build();
    private  Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
    private Artist artist2 = Artist.builder().name("Walter").build();
    private  Event event1 = Event.builder()
        .name("Birthday")
        .eventType(EventType.FESTIVAL)
        .content("feiern")
        .description("Its my Birthday")
        .artist(artist1)
        .durationInMinutes(180).build();
    private  Event event2 = Event.builder()
        .name("NoFind")
        .eventType(EventType.CONCERT)
        .content("warten")
        .description("nicht da")
        .artist(artist1).durationInMinutes(400).build();
    private   Show show1 = Show.builder().id(1L).ticketsSold(10000L).date(LocalDate.parse("17-03-2020", dateFormatter)).time(LocalTime.parse( "20:30", timeFormatter)).description("description").event(event1).hall(hall1).build();
    private   Show show2 = Show.builder().id(2L).ticketsSold(10000L).date(LocalDate.parse("15-10-2020", dateFormatter)).time(LocalTime.parse( "16:30", timeFormatter)).description("description").event(event1).hall(hall1).build();
    private  Show show3 = Show.builder().id(3L).ticketsSold(10000L).date(LocalDate.parse("15-10-2020", dateFormatter)).time(LocalTime.parse( "16:31", timeFormatter)).description("description").event(event2).hall(hall1).build();
    private Show show4 = Show.builder().id(4L).ticketsSold(10000L).date(LocalDate.parse("15-10-2021", dateFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("Top-Abend").event(event2).hall(hall2).build();

    private ShowSearchParametersDTO DETAILED_PARAMETERS = new ShowSearchParametersDTO.builder()
        .dateFrom(LocalDate.parse("16-03-2020", dateFormatter))
        .dateTo(LocalDate.parse("18-03-2020", dateFormatter))
        .eventName(event1.getName())
        .hallName(hall1.getName())
        .timeFrom(LocalTime.parse("18:00",timeFormatter))
        .timeTo(LocalTime.parse("22:00",timeFormatter)).build();
    private final ShowSearchParametersDTO NAME_ONLY_PARAMETERS = new ShowSearchParametersDTO.builder()
        .eventName("irthday").build();
    private final ShowSearchParametersDTO FROMDATE_ONLY_PARAMETERS = new ShowSearchParametersDTO.builder()
        .dateFrom(LocalDate.parse("18-03-2020", dateFormatter)).build();
    private boolean init = false;
    @Before
    public void  before(){
        if (!init) {
            locationAustria = locationRepository.save(locationAustria);
            locationGermany = locationRepository.save(locationGermany);
            artist1 = artistRepository.save(artist1);
            artist2 = artistRepository.save(artist2);
            hall1.setLocation(locationAustria);
            hall1 = hallRepository.save(hall1);
            hall2.setLocation(locationGermany);
            hall2 = hallRepository.save(hall2);
            event1.setArtist(artist1);
            event2.setArtist(artist1);
            event1 = eventRepository.save(event1);
            event2 = eventRepository.save(event2);
            show1.setHall(hall1); show1.setEvent(event1);
            show2.setHall(hall1); show1.setEvent(event1);
            show3.setHall(hall1); show3.setEvent(event2);
            show4.setHall(hall2); show4.setEvent(event2);
            show1 = showRepository.save(show1);
            show2 = showRepository.save(show2);
            show3 = showRepository.save(show3);
            show4 = showRepository.save(show4);
            init = true;
        }

    }




    @Test
    public void findShowsByStartDate(){
        List<Show> shows = showRepository.findAllShowsFiltered(FROMDATE_ONLY_PARAMETERS);
        Assert.assertEquals(3, shows.size());
        Assert.assertFalse(shows.contains(show1));
    }

    @Test
    public void findShowsByEventName(){
        List<Show> shows = showRepository.findAllShowsFiltered(NAME_ONLY_PARAMETERS);
        Assert.assertTrue(!shows.isEmpty());
        Assert.assertEquals(2 ,shows.size());
    }

    @Test
    public void findShowsByStartDate_NoFittingStartDateInDatabase_givesNoResults(){
        ShowSearchParametersDTO PARAMETER_LATE_STARTDATE = new ShowSearchParametersDTO.builder().eventName(null).hallName(null).dateFrom(LocalDate.parse("16-03-2022", dateFormatter)).dateTo(null).priceInEuroFrom(null).priceInEuroTo(null).timeTo(null).timeFrom(null).build();
        List<Show> shows = showRepository.findAllShowsFiltered(PARAMETER_LATE_STARTDATE);
        Assert.assertTrue(shows.isEmpty());

    }

    @Test
    public void findOneShowAndDontFindOthers(){
        List<Show> shows = showRepository.findAllShowsFiltered(DETAILED_PARAMETERS);
        org.junit.Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show1));
    }

    @Test
    public void findShowByMaxDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().dateTo(LocalDate.parse("18-03-2020", dateFormatter)).build();
        List<Show> shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertTrue(shows.contains(show1));
        Assert.assertEquals(1, shows.size());
    }
    @Test
    public void findShowByMinDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().dateFrom(LocalDate.parse("16-03-2020", dateFormatter)).build();
        List<Show> shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertEquals(4, shows.size());
        parameters.setDateFrom(LocalDate.parse("29-03-2020", dateFormatter));
        shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertEquals(3, shows.size());
        Assert.assertFalse(shows.contains(show1));
        parameters.setDateFrom(LocalDate.parse("16-03-2023", dateFormatter));
        shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertTrue(shows.isEmpty());
    }

    @Test
    public void findShowByHallName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().hallName(hall1.getName()).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().hallName("not contained").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(3, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByDuration(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().durationInMinutes(190).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().durationInMinutes(400).build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(2, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(2, shows.size());
        Assert.assertTrue(shows.contains(show3));
        Assert.assertTrue(shows.contains(show4));
    }

    @Test
    public void findShowByLocationName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().locationName("CheZ").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().locationName("N8chtschicht").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show4));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByCountry(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().country("austRia").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().country("United Kingdom").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(3, shows.size());
        Assert.assertTrue(shows.contains(show1));
        Assert.assertTrue(shows.contains(show2));
        Assert.assertTrue(shows.contains(show3));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByPostalCode(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().postalcode("53129").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().postalcode("53115").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show4));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void  findShowCityName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().city("Bonn").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().city("Berlin").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue((shows.contains(show4)));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByStreetName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().street("ErmekEil").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().street("Endenicher Allee").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show4));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByHouseNumber(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().houseNr(17).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().houseNr(99).build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show4));
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowsByEventId(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().eventId(event1.getId()).build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(2, shows.size());
        Assert.assertTrue(shows.contains(show1));
        Assert.assertTrue(shows.contains(show2));
    }
    @Test
    public void foundShowsAreOrderedCorrectly_byDateAndTime(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(4, shows.size());
        Assert.assertEquals(show1, shows.get(0));
        Assert.assertEquals(show2, shows.get(1));
        Assert.assertEquals(show3, shows.get(2));
        Assert.assertEquals(show4, shows.get(3));
    }


}
