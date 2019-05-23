package at.ac.tuwien.sepm.groupphase.backend.integrationtest.unit;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mapstruct.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.event.HyperlinkEvent;
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




    private  Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse 12").build();
    private  Hall hall1 = Hall.builder().id(1L).name("TopLocation").location(location1).build();
    private  Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
    private  Event event1 = Event.builder().name("Birthday").eventType(EventType.FESTIVAL).content("feiern").description("Its my Birthday").artist(artist1).build();
    private  Event event2 = Event.builder().name("NoFind").eventType(EventType.CONCERT).content("warten").description("nicht da").artist(artist1).build();
    private   Show showFound = Show.builder().id(1L).durationInMinutes(180).ticketsSold(10000L).date(LocalDate.parse("17-03-2020", dateFormatter)).time(LocalTime.parse( "20:30", timeFormatter)).description("description").event(event1).hall(hall1).build();
    private   Show showFail = Show.builder().id(2L).durationInMinutes(180).ticketsSold(10000L).date(LocalDate.parse("15-10-2020", dateFormatter)).time(LocalTime.parse( "16:30", timeFormatter)).description("description").event(event1).hall(hall1).build();
    private  Show ALTERNATE_EVENT = Show.builder().id(3L).durationInMinutes(180).ticketsSold(10000L).date(LocalDate.parse("15-10-2020", dateFormatter)).time(LocalTime.parse( "16:30", timeFormatter)).description("description").event(event2).hall(hall1).build();

    private ShowSearchParametersDTO detailedParameters2 = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setDateFrom(LocalDate.parse("16-03-2020", dateFormatter)).setDateTo(LocalDate.parse("18-03-2020", dateFormatter)).setEventName(event1.getName()).setHallName(hall1.getName()).setPriceInEuroTo(null).setPriceInEuroFrom(null).setTimeFrom(LocalTime.parse("18:00",timeFormatter)).setTimeTo(LocalTime.parse("22:00",timeFormatter)).build();
    private final ShowSearchParametersDTO PARAMETERS_NAME_ONLY = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setEventName("Birthday").setHallName(null).setDateFrom(null).setDateTo(null).setPriceInEuroFrom(null).setPriceInEuroTo(null).setTimeTo(null).setTimeFrom(null).build();
    private final ShowSearchParametersDTO PARAMETERS_FROMDATE_ONLY = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setEventName(null).setHallName(null).setDateFrom(LocalDate.parse("16-03-2020", dateFormatter)).setDateTo(null).setPriceInEuroFrom(null).setPriceInEuroTo(null).setTimeTo(null).setTimeFrom(null).build();
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
            showFound.setHall(hall1); showFound.setEvent(event1);
            showFail.setHall(hall1); showFound.setEvent(event1);
            ALTERNATE_EVENT.setHall(hall1); ALTERNATE_EVENT.setEvent(event2);
            showFound = showRepository.save(showFound);
            showFail = showRepository.save(showFail);
            ALTERNATE_EVENT = showRepository.save(ALTERNATE_EVENT);
            init = true;
        }

    }




    @Test
    public void findShowsByStartDate(){
        ShowSearchParametersDTO x = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setPriceInEuroTo(10).build();
        List<Show> shows = showRepository.findAllShowsFiltered(PARAMETERS_FROMDATE_ONLY);
        Assert.assertEquals(3, shows.size());
    }

    @Test
    public void findShowsByEventName(){
        List<Show> shows = showRepository.findAllShowsFiltered(PARAMETERS_NAME_ONLY);
        Assert.assertTrue(!shows.isEmpty());
        Assert.assertEquals(2 ,shows.size());
    }

    @Test
    public void findShowsByStartDate_NoValidStartDate(){
        ShowSearchParametersDTO PARAMETER_LATE_STARTDATE = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setEventName(null).setHallName(null).setDateFrom(LocalDate.parse("16-03-2022", dateFormatter)).setDateTo(null).setPriceInEuroFrom(null).setPriceInEuroTo(null).setTimeTo(null).setTimeFrom(null).build();
        List<Show> shows = showRepository.findAllShowsFiltered(PARAMETER_LATE_STARTDATE);
        Assert.assertTrue(shows.isEmpty());

    }

    @Test
    public void findOneShowAndDontFindOther(){
        List<Show> shows = showRepository.findAllShowsFiltered(detailedParameters2);
        org.junit.Assert.assertEquals(1, shows.size());
        Assert.assertTrue(shows.contains(showFound));
    }

    @Test
    public void findShowByMaxDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setDateTo(LocalDate.parse("18-03-2020", dateFormatter)).build();
        List<Show> shows = showRepository.findAllShowsFiltered(parameters);
        Assert.assertTrue(shows.contains(showFound));
        Assert.assertEquals(1, shows.size());
    }

    @Test
    public void findShowByHallName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setHallName(hall1.getName()).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setHallName("not contained").build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(3, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }
    @Test
    public void findShowByDuration(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setDurationInMinutes(190).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.ShowSearchParametersDTOBuilder().setDurationInMinutes(400).build();
        List<Show> shows = showRepository.findAllShowsFiltered(SuccessParameters);
        Assert.assertEquals(3, shows.size());
        shows = showRepository.findAllShowsFiltered(FailureParameters);
        Assert.assertEquals(0, shows.size());
    }



}
