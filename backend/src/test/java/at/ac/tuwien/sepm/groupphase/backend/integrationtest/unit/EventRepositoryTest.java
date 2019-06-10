//package at.ac.tuwien.sepm.groupphase.backend.integrationtest.unit;
//
//import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
//import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
//import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
//import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
//import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
//import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
//import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
//import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class EventRepositoryTest {
//    @Autowired
//    private ArtistRepository artistRepository;
//    @Autowired
//    private EventRepository eventRepository;
//
//
//
//
//    private boolean initiated = false;
//    List<Event> eventList = new ArrayList();
//    private Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
//    private Artist artist2 = Artist.builder().id(2L).name("Anton aus Tirol").build();
//    private Event event1 = Event.builder().name("Birthday").eventType(EventType.FESTIVAL).content("feiern").description("Its my Birthday").artist(artist1).durationInMinutes(180).build();
//    private  Event event2 = Event.builder().name("NoFind").eventType(EventType.CONCERT).content("feiern und warten").description("nicht da").artist(artist1).durationInMinutes(400).build();
//    private  Event event3 = Event.builder().name("Birthday").eventType(EventType.FESTIVAL).content("feiern").description("Its my Birthday").artist(artist2).durationInMinutes(400).build();
//
//    @Before
//    public void before(){
//        if(!initiated){
//            artist1 = artistRepository.save(artist1);
//            artist2 = artistRepository.save(artist2);
//            event1.setArtist(artist1);
//            event2.setArtist(artist1);
//            event3.setArtist(artist2);
//            event1 = eventRepository.save(event1);
//            event2 = eventRepository.save(event2);
//            event3 = eventRepository.save(event3);
//            initiated = true;
//        }
//    }
//
//    @Test
//    public void searchByMultipleParameters(){
//        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setArtistName(artist1.getName()).setDurationInMinutes(160).setContent(event1.getContent()).setName("Birth").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertTrue(eventList.contains(event1));
//        Assert.assertEquals(1, eventList.size());
//        parameters = EventSearchParametersDTO.builder().setContent("feie").setName("ind").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertTrue(eventList.contains(event2));
//    }
//
//    @Test
//    public void searchByEventName(){
//        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setName("Birthday").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(2, eventList.size());
//        Assert.assertTrue(eventList.contains(event1));
//        parameters = EventSearchParametersDTO.builder().setName("Rihanna").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertTrue(eventList.isEmpty());
//    }
//
//    @Test
//    public void searchByContent(){
//        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setContent("feiern").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(3, eventList.size());
//        //Assert.assertTrue(eventList.contains(event1) && eventList.contains(event2) && eventList.contains(event3));
//        parameters = EventSearchParametersDTO.builder().setContent("warten").build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(1, eventList.size());
//        Assert.assertTrue(eventList.contains(event2));
//    }
//    @Test
//    public void searchByDuration(){
//        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setDurationInMinutes(420).build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(2, eventList.size());
//        Assert.assertTrue(eventList.contains(event2));
//        Assert.assertTrue(eventList.contains(event3));
//        parameters = EventSearchParametersDTO.builder().setDurationInMinutes(160).build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(1, eventList.size());
//        Assert.assertTrue(eventList.contains(event1));
//    }
//    @Test
//    public void searchByArtist(){
//        EventSearchParametersDTO parameters = EventSearchParametersDTO.builder().setArtistName(artist1.getName()).build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(2, eventList.size());
//        Assert.assertTrue(eventList.contains(event1));
//        Assert.assertTrue(eventList.contains(event2));
//        parameters = EventSearchParametersDTO.builder().setArtistName(artist2.getName()).build();
//        eventList = eventRepository.findAllEventsFiltered(parameters);
//        Assert.assertEquals(1, eventList.size());
//        Assert.assertTrue(eventList.contains(event3));
//    }
//
//
//}
