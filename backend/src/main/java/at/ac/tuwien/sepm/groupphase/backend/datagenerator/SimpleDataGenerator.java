package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Profile("generateData")
@Component
public class SimpleDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDataGenerator.class);
    private final DateTimeFormatter formatter;

    private EventRepository eventRepository;
    private ShowRepository showRepository;
    private ArtistRepository artistRepository;
    private HallRepository hallRepository;
    private LocationRepository locationRepository;

    SimpleDataGenerator(EventRepository eventRepository, ShowRepository showRepository, ArtistRepository artistRepository, HallRepository hallRepository, LocationRepository locationRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.showRepository = showRepository;
        this.hallRepository = hallRepository;
        this.locationRepository = locationRepository;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }

    @PostConstruct
    public void generateData() {
        if (eventRepository.count() > 0 || showRepository.count() > 0 || artistRepository.count() > 0) {
            LOGGER.info("Data already generated");
        } else {
            LOGGER.info("GENERATING DATA");
            Event event1 = Event.builder().id(1L).name("Balloons N Ribbons").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event2 = Event.builder().id(2L).name("MemoriesMade").eventType(EventType.MOVIE).shows(null).artist(null).description("COMING SOON").content("content").build();
            Event event3 = Event.builder().id(3L).name("Lucky Charms").eventType(EventType.CONCERT).description("description").content("content").build();
            Event event4 = Event.builder().id(4L).name("PartyPies").eventType(EventType.MUSICAL).description("description").content("content").build();
            Event event5 = Event.builder().id(5L).name("Dreams Come True").eventType(EventType.OPERA).description("description").content(null).build();

            Event event6 = Event.builder().id(6L).name("Superathon 2019").eventType(EventType.SPORT).description("description").content("content").build();
            Event event7 = Event.builder().id(7L).name("Back in time 20'").eventType(EventType.CONCERT).description("description").content(null).build();
            Event event8 = Event.builder().id(8L).name("HouseParty").eventType(EventType.MUSICAL).shows(null).artist(null).description("COMING SOON").content(null).build();
            Event event9 = Event.builder().id(9L).name("Music Festival 21'").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event10 = Event.builder().id(10L).name("Fate & Fiesta").eventType(EventType.THEATRE).description("description").content(null).build();

            Event event11 = Event.builder().id(11L).name("Galla Evening For Many").eventType(EventType.CONCERT).shows(null).artist(null).description("COMING SOON").content(null).build();
            Event event12 = Event.builder().id(12L).name("Early Halloween").eventType(EventType.MUSICAL).shows(null).artist(null).description("COMING SOON").content("content").build();
            Event event13 = Event.builder().id(13L).name("Welcome to Valhalla").eventType(EventType.THEATRE).description("description").content(null).build();
            Event event14 = Event.builder().id(14L).name("Animecon").eventType(EventType.FESTIVAL).description("description").content(null).build();
            Event event15 = Event.builder().id(15L).name("Superbowl").eventType(EventType.SPORT).description("description").content(null).build();

            Event event16 = Event.builder().id(16L).name("Carnival of the 100 seasons").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event17 = Event.builder().id(17L).name("Fest Frost Fast Fist").eventType(EventType.CONCERT).description("description").content(null).build();
            Event event18 = Event.builder().id(18L).name("LetMeFixYa").eventType(EventType.MOVIE).artist(null).description("COMING SOON").content(null).build();
            Event event19 = Event.builder().id(19L).name("Quite a Quiet Quiz").eventType(EventType.OPERA).description("description").content("content").build();
            Event event20 = Event.builder().id(20L).name("Theatre Seven Dwarfs").eventType(EventType.THEATRE).description("description").content(null).build();

            Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
            Artist artist2 = Artist.builder().id(2L).name("Benny Loshoto").build();
            Artist artist3 = Artist.builder().id(3L).name("Reggy").build();
            Artist artist4 = Artist.builder().id(4L).name("Tonio Bananas").build();
            Artist artist5 = Artist.builder().id(5L).name("Lady Galla").build();
            Artist artist6 = Artist.builder().id(6L).name("Pinna Colada").build();
            Artist artist7 = Artist.builder().id(7L).name("Steve Wonder").eventParticipations(null).build();

            artist1.setEventParticipations(new ArrayList<>() {{
                add(event1);
                add(event3);
                add(event4);
                add(event5);
            }});
            artist2.setEventParticipations(new ArrayList<>() {{
                add(event6);
                add(event7);
                add(event9);
                add(event10);
            }});
            artist3.setEventParticipations(new ArrayList<>() {{
                add(event13);
                add(event14);
            }});
            artist4.setEventParticipations(new ArrayList<>() {{
                add(event15);
                add(event16);
                add(event17);
            }});
            artist5.setEventParticipations(new ArrayList<>() {{
                add(event19);
            }});
            artist6.setEventParticipations(new ArrayList<>() {{
                add(event20);
            }});

            event1.setArtist(artist1);
            event3.setArtist(artist1);
            event4.setArtist(artist1);
            event5.setArtist(artist1);
            event6.setArtist(artist2);
            event7.setArtist(artist2);
            event9.setArtist(artist2);
            event10.setArtist(artist2);
            event13.setArtist(artist3);
            event14.setArtist(artist3);
            event15.setArtist(artist4);
            event16.setArtist(artist4);
            event17.setArtist(artist4);
            event19.setArtist(artist5);
            event20.setArtist(artist6);

            Show show1 = Show.builder().id(1L).durationInMinutes(180).ticketsSold(10000L).dateTime(LocalDateTime.parse("09-10-2019 10:30", formatter)).description("description").build();
            Show show2 = Show.builder().id(2L).durationInMinutes(200).ticketsSold(2000L).dateTime(LocalDateTime.parse("15-11-2019 20:30", formatter)).description("description").build();
            Show show3 = Show.builder().id(3L).durationInMinutes(100).ticketsSold(1500L).dateTime(LocalDateTime.parse("14-08-2019 17:30", formatter)).description("description").build();
            Show show4 = Show.builder().id(4L).durationInMinutes(50).ticketsSold(150L).dateTime(LocalDateTime.parse("07-07-2019 19:00", formatter)).description("description").build();
            Show show5 = Show.builder().id(5L).durationInMinutes(60).ticketsSold(200L).dateTime(LocalDateTime.parse("01-01-2019 19:30", formatter)).description("description").build();

            Show show6 = Show.builder().id(6L).durationInMinutes(110).ticketsSold(1000L).dateTime(LocalDateTime.parse("17-09-2029 21:00", formatter)).description("description").build();
            Show show7 = Show.builder().id(7L).durationInMinutes(120).ticketsSold(500L).dateTime(LocalDateTime.parse("19-02-2019 23:00", formatter)).description("description").build();
            Show show8 = Show.builder().id(8L).durationInMinutes(40).ticketsSold(400L).dateTime(LocalDateTime.parse("04-04-2019 23:30", formatter)).description("description").build();
            Show show9 = Show.builder().id(9L).durationInMinutes(130).ticketsSold(500L).dateTime(LocalDateTime.parse("22-06-2019 14:30", formatter)).description("description").build();
            Show show10 = Show.builder().id(10L).durationInMinutes(130).ticketsSold(500L).dateTime(LocalDateTime.parse("29-12-2019 15:30", formatter)).description("description").build();

            Show show11 = Show.builder().id(11L).durationInMinutes(100).ticketsSold(2000L).dateTime(LocalDateTime.parse("16-07-2019 10:30", formatter)).description("description").build();
            Show show12 = Show.builder().id(12L).durationInMinutes(110).ticketsSold(1800L).dateTime(LocalDateTime.parse("04-12-2019 12:00", formatter)).description("description").build();
            Show show13 = Show.builder().id(13L).durationInMinutes(120).ticketsSold(1200L).dateTime(LocalDateTime.parse("03-11-2019 12:30", formatter)).description("description").build();
            Show show14 = Show.builder().id(14L).durationInMinutes(90).ticketsSold(1400L).dateTime(LocalDateTime.parse("17-02-2019 13:00", formatter)).description("description").build();
            Show show15 = Show.builder().id(15L).durationInMinutes(70).ticketsSold(100L).dateTime(LocalDateTime.parse("23-02-2019 15:00", formatter)).description("description").build();

            Show show16 = Show.builder().id(16L).durationInMinutes(70).ticketsSold(200L).dateTime(LocalDateTime.parse("28-07-2019 17:30", formatter)).description("description").build();
            Show show17 = Show.builder().id(17L).durationInMinutes(80).ticketsSold(400L).dateTime(LocalDateTime.parse("30-08-2019 15:00", formatter)).description("description").build();
            Show show18 = Show.builder().id(18L).durationInMinutes(80).ticketsSold(100L).dateTime(LocalDateTime.parse("30-03-2019 11:00", formatter)).description("description").build();
            Show show19 = Show.builder().id(19L).durationInMinutes(80).ticketsSold(500L).dateTime(LocalDateTime.parse("11-05-2019 19:00", formatter)).description("description").build();
            Show show20 = Show.builder().id(20L).durationInMinutes(80).ticketsSold(500L).dateTime(LocalDateTime.parse("06-01-2019 21:30", formatter)).description("description").build();

            Show show21 = Show.builder().id(21L).durationInMinutes(190).ticketsSold(12000L).dateTime(LocalDateTime.parse("05-02-2019 21:00", formatter)).description("description").build();
            Show show22 = Show.builder().id(22L).durationInMinutes(200).ticketsSold(25000L).dateTime(LocalDateTime.parse("19-03-2019 23:30", formatter)).description("description").build();
            Show show23 = Show.builder().id(23L).durationInMinutes(150).ticketsSold(1000L).dateTime(LocalDateTime.parse("24-04-2019 23:30", formatter)).description("description").build();
            Show show24 = Show.builder().id(24L).durationInMinutes(160).ticketsSold(11000L).dateTime(LocalDateTime.parse("22-08-2019 23:00", formatter)).description("description").build();
            Show show25 = Show.builder().id(25L).durationInMinutes(130).ticketsSold(1800L).dateTime(LocalDateTime.parse("23-11-2019 18:30", formatter)).description("description").build();

            show1.setEvent(event1);
            show2.setEvent(event3);
            show24.setEvent(event3);
            show3.setEvent(event4);
            show15.setEvent(event4);
            show4.setEvent(event5);
            show16.setEvent(event5);
            show5.setEvent(event6);
            show6.setEvent(event7);
            show17.setEvent(event7);
            show25.setEvent(event7);
            show7.setEvent(event9);
            show8.setEvent(event10);
            show9.setEvent(event13);
            show10.setEvent(event14);
            show11.setEvent(event15);
            show19.setEvent(event15);
            show12.setEvent(event16);
            show18.setEvent(event16);
            show20.setEvent(event16);
            show21.setEvent(event17);
            show13.setEvent(event19);
            show22.setEvent(event19);
            show14.setEvent(event20);
            show23.setEvent(event20);

            /* //Gives Nested exception is java.lang.StackOverflowError (Don't know why)

            event1.setShows(new ArrayList<>() {{
                add(show1);
            }});
            event3.setShows(new ArrayList<>() {{
                add(show2);
                add(show24);
            }});
            event4.setShows(new ArrayList<>() {{
                add(show3);
                add(show15);
            }});
            event5.setShows(new ArrayList<>() {{
                add(show4);
                add(show16);
            }});
            event6.setShows(new ArrayList<>() {{
                add(show5);
            }});
            event7.setShows(new ArrayList<>() {{
                add(show6);
                add(show17);
                add(show25);
            }});
            event9.setShows(new ArrayList<>() {{
                add(show7);
            }});
            event10.setShows(new ArrayList<>() {{
                add(show8);
            }});
            event13.setShows(new ArrayList<>() {{
                add(show9);
            }});
            event14.setShows(new ArrayList<>() {{
                add(show10);
            }});
            event15.setShows(new ArrayList<>() {{
                add(show11);
                add(show19);
            }});
            event16.setShows(new ArrayList<>() {{
                add(show12);
                add(show18);
                add(show20);
            }});
            event17.setShows(new ArrayList<>() {{
                add(show21);
            }});
            event19.setShows(new ArrayList<>() {{
                add(show13);
                add(show22);
            }});
            event20.setShows(new ArrayList<>() {{
                add(show14);
                add(show23);
            }});
            */

            Hall hall1 = Hall.builder().id(1L).name("Hall1").build();
            Hall hall2 = Hall.builder().id(2L).name("Hall2").build();
            Hall hall3 = Hall.builder().id(3L).name("Hall3").build();
            Hall hall4 = Hall.builder().id(4L).name("Hall4").build();
            Hall hall5 = Hall.builder().id(5L).name("Hall5").build();
            Hall hall6 = Hall.builder().id(6L).name("Hall6").build();
            Hall hall7 = Hall.builder().id(7L).name("Hall7").build();
            Hall hall8 = Hall.builder().id(8L).name("Hall8").build();
            Hall hall9 = Hall.builder().id(9L).name("Hall9").build();
            Hall hall10 = Hall.builder().id(10L).name("Hall10").build();

            show1.setHall(hall1);
            show2.setHall(hall2);
            show3.setHall(hall3);
            show4.setHall(hall4);
            show5.setHall(hall5);
            show6.setHall(hall6);
            show7.setHall(hall7);
            show8.setHall(hall8);
            show9.setHall(hall9);
            show10.setHall(hall10);
            show11.setHall(hall1);
            show12.setHall(hall1);
            show13.setHall(hall2);
            show14.setHall(hall3);
            show15.setHall(hall4);
            show16.setHall(hall4);
            show17.setHall(hall5);
            show18.setHall(hall5);
            show19.setHall(hall5);
            show20.setHall(hall6);
            show21.setHall(hall7);
            show22.setHall(hall8);
            show23.setHall(hall9);
            show24.setHall(hall10);
            show25.setHall(hall10);

            Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").postalcode("1090").street("Tendlergasse 12").build();
            Location location2 = Location.builder().id(2L).country("Austria").city("Vienna").postalcode("1220").street("Josef Baumann Gasse 8").build();
            Location location3 = Location.builder().id(3L).country("Austria").city("Vienna").postalcode("1010").street("Maria-Theresien-Platz").build();
            Location location4 = Location.builder().id(4L).country("Austria").city("Vienna").postalcode("1211").street("Siemensstraße 90").description("No Halls").build();
            Location location5 = Location.builder().id(5L).country("Austria").city("Vienna").postalcode("1010").street("Burgring 7").description("No Halls").build();
            Location location6 = Location.builder().id(6L).country("Austria").city("Vienna").postalcode("1030").street("Prinz Eugen-Straße 27").build();
            Location location7 = Location.builder().id(7L).country("Austria").city("Vienna").postalcode("1020").street("Oswald-Thomas-Platz 1").build();
            Location location8 = Location.builder().id(8L).country("Austria").city("Vienna").postalcode("1130").street("Schönbrunner Schloßstraße").build();
            Location location9 = Location.builder().id(9L).country("Austria").city("Vienna").postalcode("1060").street("Gumpendorfer Straße 142").build();
            Location location10 = Location.builder().id(10L).country("Austria").city("Vienna").postalcode("1050").street("Burggasse 121").description("No Halls").build();

            hall1.setLocation(location1);
            hall2.setLocation(location2);
            hall3.setLocation(location3);
            hall4.setLocation(location1);
            hall5.setLocation(location7);
            hall6.setLocation(location6);
            hall7.setLocation(location7);
            hall8.setLocation(location8);
            hall9.setLocation(location9);
            hall10.setLocation(location2);

            artistRepository.saveAll(Arrays.asList(artist1, artist2, artist3, artist4, artist5, artist6, artist7));
            eventRepository.saveAll(Arrays.asList(event1, event2, event3, event4, event5, event6, event7, event8, event9, event10, event11, event12, event13, event13, event14, event15, event16, event17, event18, event19, event20));
            locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9, location10));
            hallRepository.saveAll(Arrays.asList(hall1, hall2, hall3, hall4, hall5, hall6, hall7, hall8, hall9, hall10));
            showRepository.saveAll(Arrays.asList(show1, show2, show3, show4, show5, show6, show7, show7, show8, show9, show10, show11, show12, show13, show14, show15, show16, show17, show18, show19, show20, show21, show22, show23, show24, show25));
        }
    }

}
