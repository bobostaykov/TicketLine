package at.ac.tuwien.sepm.groupphase.backend.unit.persistance;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShowRepositoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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
    @Autowired
    private PricePatternRepository pricePatternRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private Location locationAustria = Location.builder().id(1L).country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse ").locationName("Stadthalle").build();
    private Location locationGermany = Location.builder().country("Germany").city("Bonn").postalCode("53129").street("Ermekeilstrasse").locationName("chez Horst").build();
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
    private HashMap<PriceCategory, Double> priceMap1 = new HashMap<>();
    private HashMap<PriceCategory, Double> priceMap2 = new HashMap<>();
    private PricePattern pricePattern1 = PricePattern.builder().setName("normal").createPricePattern();
    private PricePattern pricePattern2 = PricePattern.builder().setName("normal2").createPricePattern();

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
            priceMap1.put(PriceCategory.CHEAP, 10.0); priceMap1.put(PriceCategory.AVERAGE, 20.0); priceMap1.put(PriceCategory.EXPENSIVE, 40.0);
            pricePattern1.setPriceMapping(priceMap1);
            pricePattern1 = pricePatternRepository.save(pricePattern1);
            priceMap2.put(PriceCategory.CHEAP, 10.0); priceMap2.put(PriceCategory.AVERAGE, 15.0); priceMap2.put(PriceCategory.EXPENSIVE, 20.0);
            pricePattern2.setPriceMapping(priceMap2);
            pricePattern2 = pricePatternRepository.save(pricePattern2);
            show1.setPricePattern(pricePattern1);
            show2.setPricePattern(pricePattern1);
            show3.setPricePattern(pricePattern1);
            show4.setPricePattern(pricePattern2);

            locationAustria = locationRepository.save(locationAustria);
            locationGermany = locationRepository.save(locationGermany);
            artist1 = artistRepository.save(artist1);
            artist2 = artistRepository.save(artist2);
            hall1.setLocation(locationAustria);
            hall1 = hallRepository.save(hall1);
            hall2.setLocation(locationGermany);
            hall2 = hallRepository.save(hall2);
            event1.setArtist(artist1);
            event2.setArtist(artist2);
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
        Page<Show> showPage = showRepository.findAllShowsFiltered(FROMDATE_ONLY_PARAMETERS, PageRequest.of(0,10));
        Assert.assertEquals(3, showPage.getContent().size());
        Assert.assertFalse(showPage.getContent().contains(show1));
    }

    @Test
    public void findShowsByEventName(){
        Page<Show> showPage = showRepository.findAllShowsFiltered(NAME_ONLY_PARAMETERS, PageRequest.of(0,10));
        Assert.assertTrue(showPage.hasContent());
        Assert.assertEquals(2 ,showPage.getContent().size());
    }

    @Test
    public void findShowsByArtistName(){
        Page<Show> showPage = showRepository.findAllShowsFiltered(ShowSearchParametersDTO.builder().artistName("hol").build(), PageRequest.of(0,10));
     //   Assert.assertTrue(showPage.hasContent());
        Assert.assertEquals(2 ,showPage.getContent().size());    }

    @Test
    public void findShowsByStartDate_NoFittingStartDateInDatabase_givesNoResults(){
        ShowSearchParametersDTO PARAMETER_LATE_STARTDATE = new ShowSearchParametersDTO.builder().eventName(null).hallName(null).dateFrom(LocalDate.parse("16-03-2022", dateFormatter)).dateTo(null).priceInEuroFrom(null).priceInEuroTo(null).timeTo(null).timeFrom(null).build();
        exception.expect(NotFoundException.class);
        Page<Show> showPage = showRepository.findAllShowsFiltered(PARAMETER_LATE_STARTDATE, PageRequest.of(0,10));


    }

    @Test
    public void findOneShowAndDontFindOthers(){
        Page<Show> showPage = showRepository.findAllShowsFiltered(DETAILED_PARAMETERS, PageRequest.of(0,10));
        Assert.assertEquals(1, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show1));
    }

    @Test
    public void findShowByMaxDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().dateTo(LocalDate.parse("18-03-2020", dateFormatter)).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(parameters, PageRequest.of(0,10));
        Assert.assertTrue(showPage.getContent().contains(show1));
        Assert.assertEquals(1, showPage.getContent().size());
    }
    @Test
    public void findShowByMinDate(){
        ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder().dateFrom(LocalDate.parse("16-03-2020", dateFormatter)).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(parameters, PageRequest.of(0,10));
        Assert.assertEquals(4, showPage.getContent().size());
        parameters.setDateFrom(LocalDate.parse("29-03-2020", dateFormatter));
        showPage = showRepository.findAllShowsFiltered(parameters, PageRequest.of(0,10));
        Assert.assertEquals(3, showPage.getContent().size());
        Assert.assertFalse(showPage.getContent().contains(show1));
        parameters.setDateFrom(LocalDate.parse("16-03-2023", dateFormatter));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(parameters, PageRequest.of(0,10));
    }

    @Test
    public void findShowByHallName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().hallName(hall1.getName()).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().hallName("not contained").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters, PageRequest.of(0,10));
        Assert.assertEquals(3, showPage.getContent().size());
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters, PageRequest.of(0,10));

    }
    @Test
    public void findShowByDuration(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().durationInMinutes(190).build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().durationInMinutes(400).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(2, showPage.getContent().size());
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
        Assert.assertEquals(2, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show3));
        Assert.assertTrue(showPage.getContent().contains(show4));
    }

    @Test
    public void findShowByLocationName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().locationName("CheZ").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().locationName("N8chtschicht").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(1, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show4));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
    }
    @Test
    public void findShowByCountry(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().country("austRia").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().country("United Kingdom").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(3, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show1));
        Assert.assertTrue(showPage.getContent().contains(show2));
        Assert.assertTrue(showPage.getContent().contains(show3));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
    }
    @Test
    public void findShowByPostalCode(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().postalcode("53129").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().postalcode("53115").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(1, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show4));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
    }
    @Test
    public void  findShowCityName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().city("Bonn").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().city("Berlin").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(1, showPage.getContent().size());
        Assert.assertTrue((showPage.getContent().contains(show4)));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
    }
    @Test
    public void findShowByStreetName(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().street("ErmekEil").build();
        ShowSearchParametersDTO FailureParameters = new ShowSearchParametersDTO.builder().street("Endenicher Allee").build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(1, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show4));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(FailureParameters,PageRequest.of(0,10));
    }
    @Test
    public void findShowsByEventId(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().eventId(event1.getId()).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(2, showPage.getContent().size());
        Assert.assertTrue(showPage.getContent().contains(show1));
        Assert.assertTrue(showPage.getContent().contains(show2));
    }
    @Test
    public void foundShowsAreOrderedCorrectly_byDateAndTime(){
        ShowSearchParametersDTO SuccessParameters = new ShowSearchParametersDTO.builder().build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(SuccessParameters,PageRequest.of(0,10));
        Assert.assertEquals(4, showPage.getContent().size());
        Assert.assertEquals(show1, showPage.getContent().get(0));
        Assert.assertEquals(show2, showPage.getContent().get(1));
        Assert.assertEquals(show3, showPage.getContent().get(2));
        Assert.assertEquals(show4, showPage.getContent().get(3));
    }

    @Test
    public void findShowByMaxPrice_filtersCorrectly_andIsOrderedCorrectly(){
        ShowSearchParametersDTO successParameters = new ShowSearchParametersDTO.builder().priceInEuroTo(30).build();
        ShowSearchParametersDTO failureParameters = new ShowSearchParametersDTO.builder().priceInEuroTo(50).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(successParameters,PageRequest.of(0,10));
        Assert.assertEquals(4, showPage.getContent().size());
        Assert.assertEquals(show1, showPage.getContent().get(0));
        Assert.assertEquals(show2, showPage.getContent().get(1));
        Assert.assertEquals(show3, showPage.getContent().get(2));;

    }
    @Test
    public void findShowByMinPrice_filtersCorrectly_andIsOrderedCorrectly(){
        ShowSearchParametersDTO successParameters = new ShowSearchParametersDTO.builder().priceInEuroFrom(30).build();
        ShowSearchParametersDTO failureParameters = new ShowSearchParametersDTO.builder().priceInEuroFrom(50).build();
        Page<Show> showPage = showRepository.findAllShowsFiltered(successParameters,PageRequest.of(0,10));
        Assert.assertEquals(3, showPage.getContent().size());
        Assert.assertEquals(show1, showPage.getContent().get(0));
        Assert.assertEquals(show2, showPage.getContent().get(1));
        Assert.assertEquals(show3, showPage.getContent().get(2));
        exception.expect(NotFoundException.class);
        showPage = showRepository.findAllShowsFiltered(failureParameters,PageRequest.of(0,10));
    }


}
