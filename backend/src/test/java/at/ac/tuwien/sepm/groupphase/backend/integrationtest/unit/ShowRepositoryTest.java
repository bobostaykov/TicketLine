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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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




    private  Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse 12").build();
    private  Hall hall1 = Hall.builder().id(1L).name("TopLocation").location(location1).build();
    private  Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
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
    private  Show show3 = Show.builder().id(3L).ticketsSold(10000L).date(LocalDate.parse("15-10-2020", dateFormatter)).time(LocalTime.parse( "16:30", timeFormatter)).description("description").event(event2).hall(hall1).build();

    private ShowSearchParametersDTO DETAILED_PARAMETERS = new ShowSearchParametersDTO.builder()
        .setDateFrom(LocalDate.parse("16-03-2020", dateFormatter))
        .setDateTo(LocalDate.parse("18-03-2020", dateFormatter))
        .setEventName(event1.getName())
        .setHallName(hall1.getName())
        .setTimeFrom(LocalTime.parse("18:00",timeFormatter))
        .setTimeTo(LocalTime.parse("22:00",timeFormatter)).build();
    private final ShowSearchParametersDTO NAME_ONLY_PARAMETERS = new ShowSearchParametersDTO.builder()
        .setEventName("irthday").build();
    private final ShowSearchParametersDTO FROMDATE_ONLY_PARAMETERS = new ShowSearchParametersDTO.builder()
        .setDateFrom(LocalDate.parse("16-03-2020", dateFormatter)).build();
    private boolean init = false;
    @Before
    public void  before(){
        if (!init) {
            location1 = locationRepository.save(location1);
            artist1 = artistRepository.save(artist1);
            hall1.setLocation(location1);
            hall1 = hallRepository.save(hall1);
            event1.setArtist(artist1);
            event2.setArtist(artist1);
            event1 = eventRepository.save(event1);
            event2 = eventRepository.save(event2);
            show1.setHall(hall1); show1.setEvent(event1);
            show2.setHall(hall1); show1.setEvent(event1);
            show3.setHall(hall1); show3.setEvent(event2);
            show1 = showRepository.save(show1);
            show2 = showRepository.save(show2);
            show3 = showRepository.save(show3);
            init = true;
        }

    }




    @Test
    public void findShowsByStartDate(){
        List<Show> shows = showRepository.findAllShowsFiltered(FROMDATE_ONLY_PARAMETERS);
        Assert.assertEquals(3, shows.size());
    }

    @Test
    public void findShowsByEventName(){
        List<Show> shows = showRepository.findAllShowsFiltered(NAME_ONLY_PARAMETERS);
        Assert.assertTrue(!shows.isEmpty());
        Assert.assertEquals(2 ,shows.size());
    }

    @Test
    public void findShowsByStartDate_NoValidStartDate(){
        ShowSearchParametersDTO PARAMETER_LATE_STARTDATE = new ShowSearchParametersDTO.builder().setEventName(null).setHallName(null).setDateFrom(LocalDate.parse("16-03-2022", dateFormatter)).setDateTo(null).setPriceInEuroFrom(null).setPriceInEuroTo(null).setTimeTo(null).setTimeFrom(null).build();
        List<Show> shows = showRepository.findAllShowsFiltered(PARAMETER_LATE_STARTDATE);
        Assert.assertTrue(shows.isEmpty());

    }

    @Test
    public void findOneShowAndDontFindOther(){
        List<Show> shows = showRepository.findAllShowsFiltered(DETAILED_PARAMETERS);
        org.junit.Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show1));
    }

    @Test
    public void findShowByMaxDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().setDateTo(LocalDate.parse("18-03-2020", dateFormatter)).build();
        List<Show> shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertTrue(shows.contains(show1));
        Assert.assertEquals(1, shows.size());
    }
    @Test
    public void findShowByMinDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().setDateFrom(LocalDate.parse("16-03-2020", dateFormatter)).build();
        List<Show> shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertEquals(3, shows.size());
        parameters.setDateFrom(LocalDate.parse("29-03-2020", dateFormatter));
        shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertEquals(2, shows.size());
        Assert.assertFalse(shows.contains(show1));
        parameters.setDateFrom(LocalDate.parse("16-03-2021", dateFormatter));
        shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertTrue(shows.isEmpty());
    }

    @Test
    public void findShowByHallName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().setHallName(hall1.getName()).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().setHallName("not contained").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(3, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByDuration(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().setDurationInMinutes(190).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().setDurationInMinutes(400).build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(2, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(show3));
    }



}
