package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/*
    CLASS IS NO LONGER USED
 */
@Profile("generateData")
@Component
public class SimpleDataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

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
        dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    //@PostConstruct
    public void generateData() {
        /*
        if (eventRepository.count() > 0 || showRepository.count() > 0 || artistRepository.count() > 0) {
            LOGGER.info("Data already generated");
        } else {
            LOGGER.info("GENERATING DATA");
            Event event1 = Event.builder().id(1L).name("Balloons N Ribbons").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event2 = Event.builder().id(2L).name("MemoriesMade").eventType(EventType.MOVIE).artist(null).description("COMING SOON").content("content").build();
            Event event3 = Event.builder().id(3L).name("Lucky Charms").eventType(EventType.CONCERT).description("description").content("content").build();
            Event event4 = Event.builder().id(4L).name("PartyPies").eventType(EventType.MUSICAL).description("description").content("content").build();
            Event event5 = Event.builder().id(5L).name("Dreams Come True").eventType(EventType.OPERA).description("description").content(null).build();

            Event event6 = Event.builder().id(6L).name("Superathon 2019").eventType(EventType.SPORT).description("description").content("content").build();
            Event event7 = Event.builder().id(7L).name("Back in time 20'").eventType(EventType.CONCERT).description("description").content(null).build();
            Event event8 = Event.builder().id(8L).name("HouseParty").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content(null).build();
            Event event9 = Event.builder().id(9L).name("Music Festival 21'").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event10 = Event.builder().id(10L).name("Fate & Fiesta").eventType(EventType.THEATRE).description("description").content(null).build();

            Event event11 = Event.builder().id(11L).name("Galla Evening For Many").eventType(EventType.CONCERT).artist(null).description("COMING SOON").content(null).build();
            Event event12 = Event.builder().id(12L).name("Early Halloween").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content("content").build();
            Event event13 = Event.builder().id(13L).name("Welcome to Valhalla").eventType(EventType.THEATRE).description("description").content(null).build();
            Event event14 = Event.builder().id(14L).name("Animecon").eventType(EventType.FESTIVAL).description("description").content(null).build();
            Event event15 = Event.builder().id(15L).name("Superbowl").eventType(EventType.SPORT).description("description").content(null).build();

            Event event16 = Event.builder().id(16L).name("Carnival of the 100 seasons").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event17 = Event.builder().id(17L).name("Fest Frost Fast Fist").eventType(EventType.CONCERT).description("description").content(null).build();
            Event event18 = Event.builder().id(18L).name("LetMeFixYa").eventType(EventType.MOVIE).artist(null).description("COMING SOON").content(null).build();
            Event event19 = Event.builder().id(19L).name("Quite a Quiet Quiz").eventType(EventType.OPERA).description("description").content("content").build();
            Event event20 = Event.builder().id(20L).name("Theatre Seven Dwarfs").eventType(EventType.THEATRE).description("description").content(null).build();

            Event event21 = Event.builder().id(21L).name("Guns and Ships").eventType(EventType.OPERA).description("description").content("content").build();
            Event event22 = Event.builder().id(22L).name("Bad Girls Play With Fire").eventType(EventType.OPERA).artist(null).description("COMING SOON").content("content").build();
            Event event23 = Event.builder().id(23L).name("How to Party").eventType(EventType.CONCERT).description("description").content("content").build();
            Event event24 = Event.builder().id(24L).name("Magiciancon 2019").eventType(EventType.MUSICAL).description("description").content("content").build();
            Event event25 = Event.builder().id(25L).name("Zombie Mashup").eventType(EventType.OPERA).description("description").content(null).build();

            Event event26 = Event.builder().id(26L).name("Marathon For The Youngs").eventType(EventType.SPORT).description("description").content("content").build();
            Event event27 = Event.builder().id(27L).name("HighSchool Reopening'").eventType(EventType.CONCERT).description("description").content(null).build();
            Event event28 = Event.builder().id(28L).name("Dirty Diana").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content(null).build();
            Event event29 = Event.builder().id(29L).name("Coachella 2020").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event30 = Event.builder().id(30L).name("Some Old Time Roads").eventType(EventType.THEATRE).description("description").content(null).build();

            Event event31 = Event.builder().id(31L).name("Yoga for 1000 people").eventType(EventType.SPORT).artist(null).description("COMING SOON").content(null).build();
            Event event32 = Event.builder().id(32L).name("Cringe Party 2020").eventType(EventType.FESTIVAL).artist(null).description("COMING SOON").content("content").build();
            Event event33 = Event.builder().id(33L).name("When God meets Groundmother").eventType(EventType.MOVIE).description("description").content(null).build();
            Event event34 = Event.builder().id(34L).name("Doggy with no style").eventType(EventType.FESTIVAL).description("description").content(null).build();
            Event event35 = Event.builder().id(35L).name("Uncle Benny").eventType(EventType.MOVIE).description("description").content(null).build();

            Event event36 = Event.builder().id(36L).name("Habibis United").eventType(EventType.FESTIVAL).description("description").content("content").build();
            Event event37 = Event.builder().id(37L).name("Tomato Sause").eventType(EventType.THEATRE).description("description").content(null).build();
            Event event38 = Event.builder().id(38L).name("Bobby Hobby").eventType(EventType.THEATRE).description("description").content(null).build();
            Event event39 = Event.builder().id(39L).name("Best Of Opera").eventType(EventType.OPERA).description("description").content("content").build();
            Event event40 = Event.builder().id(40L).name("Mozart Regrets Himself").eventType(EventType.CONCERT).description("description").content(null).build();

            Artist artist1 = Artist.builder().id(1L).name("Zara Holland").build();
            Artist artist2 = Artist.builder().id(2L).name("Benny Loshoto").build();
            Artist artist3 = Artist.builder().id(3L).name("Reggy").build();
            Artist artist4 = Artist.builder().id(4L).name("Tonio Bananas").build();
            Artist artist5 = Artist.builder().id(5L).name("Lady Galla").build();
            Artist artist6 = Artist.builder().id(6L).name("Pinna Colada").build();
            Artist artist7 = Artist.builder().id(7L).name("Steve Wonder").build();
            Artist artist8 = Artist.builder().id(8L).name("Johny Bravo").build();
            Artist artist9 = Artist.builder().id(9L).name("Perry Dont Me Merry").build();
            Artist artist10 = Artist.builder().id(10L).name("Real Thug").build();
            Artist artist11 = Artist.builder().id(11L).name("3Beta").build();
            Artist artist12 = Artist.builder().id(12L).name("Rice").build();
            Artist artist13 = Artist.builder().id(13L).name("Koch").build();

            /*
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
            artist8.setEventParticipations(new ArrayList<>() {{
                add(event21);
                add(event23);
                add(event24);
                add(event25);
            }});
            artist9.setEventParticipations(new ArrayList<>() {{
                add(event26);
            }});
            artist10.setEventParticipations(new ArrayList<>() {{
                add(event27);
                add(event40);
            }});
            artist11.setEventParticipations(new ArrayList<>() {{
                add(event33);
                add(event39);
            }});
            artist12.setEventParticipations(new ArrayList<>() {{
                add(event29);
                add(event34);
                add(event38);
            }});
            artist13.setEventParticipations(new ArrayList<>() {{
                add(event30);
                add(event35);
                add(event36);
                add(event37);
            }});
            */
        /*
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
            event21.setArtist(artist8);
            event23.setArtist(artist8);
            event24.setArtist(artist8);
            event25.setArtist(artist8);
            event26.setArtist(artist9);
            event27.setArtist(artist10);
            event29.setArtist(artist12);
            event30.setArtist(artist13);
            event33.setArtist(artist11);
            event34.setArtist(artist12);
            event35.setArtist(artist13);
            event36.setArtist(artist13);
            event37.setArtist(artist13);
            event38.setArtist(artist12);
            event39.setArtist(artist11);
            event40.setArtist(artist10);

            Show show1 = Show.builder().id(1L).durationInMinutes(180).ticketsSold(10000L).date(LocalDate.parse("09-10-2019", dateFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("description").build();
            Show show2 = Show.builder().id(2L).durationInMinutes(200).ticketsSold(2000L).date(LocalDate.parse("15-11-2019", dateFormatter)).time(LocalTime.parse( "20:30", timeFormatter)).description("description").build();
            Show show3 = Show.builder().id(3L).durationInMinutes(100).ticketsSold(1500L).date(LocalDate.parse("14-08-2019", dateFormatter)).time(LocalTime.parse( "17:30", timeFormatter)).description("description").build();
            Show show4 = Show.builder().id(4L).durationInMinutes(50).ticketsSold(150L).date(LocalDate.parse("07-07-2019", dateFormatter)).time(LocalTime.parse( "19:00", timeFormatter)).description("description").build();
            Show show5 = Show.builder().id(5L).durationInMinutes(60).ticketsSold(200L).date(LocalDate.parse("01-01-2019", dateFormatter)).time(LocalTime.parse( "19:30", timeFormatter)).description("description").build();

            Show show6 = Show.builder().id(6L).durationInMinutes(110).ticketsSold(1000L).date(LocalDate.parse("17-09-2029", dateFormatter)).time(LocalTime.parse( "21:00", timeFormatter)).description("description").build();
            Show show7 = Show.builder().id(7L).durationInMinutes(120).ticketsSold(500L).date(LocalDate.parse("19-02-2019", dateFormatter)).time(LocalTime.parse( "23:00", timeFormatter)).description("description").build();
            Show show8 = Show.builder().id(8L).durationInMinutes(40).ticketsSold(400L).date(LocalDate.parse("04-04-2019", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show9 = Show.builder().id(9L).durationInMinutes(130).ticketsSold(500L).date(LocalDate.parse("22-06-2019", dateFormatter)).time(LocalTime.parse( "14:30", timeFormatter)).description("description").build();
            Show show10 = Show.builder().id(10L).durationInMinutes(130).ticketsSold(500L).date(LocalDate.parse("29-12-2019", dateFormatter)).time(LocalTime.parse( "15:30", timeFormatter)).description("description").build();

            Show show11 = Show.builder().id(11L).durationInMinutes(100).ticketsSold(2000L).date(LocalDate.parse("16-07-2019", dateFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("description").build();
            Show show12 = Show.builder().id(12L).durationInMinutes(110).ticketsSold(1800L).date(LocalDate.parse("04-12-2019", dateFormatter)).time(LocalTime.parse( "12:00", timeFormatter)).description("description").build();
            Show show13 = Show.builder().id(13L).durationInMinutes(120).ticketsSold(1200L).date(LocalDate.parse("03-11-2019", dateFormatter)).time(LocalTime.parse( "12:30", timeFormatter)).description("description").build();
            Show show14 = Show.builder().id(14L).durationInMinutes(90).ticketsSold(1400L).date(LocalDate.parse("17-02-2019", dateFormatter)).time(LocalTime.parse( "13:00", timeFormatter)).description("description").build();
            Show show15 = Show.builder().id(15L).durationInMinutes(70).ticketsSold(100L).date(LocalDate.parse("23-02-2019", dateFormatter)).time(LocalTime.parse( "15:00", timeFormatter)).description("description").build();

            Show show16 = Show.builder().id(16L).durationInMinutes(70).ticketsSold(200L).date(LocalDate.parse("28-07-2019", dateFormatter)).time(LocalTime.parse( "17:30", timeFormatter)).description("description").build();
            Show show17 = Show.builder().id(17L).durationInMinutes(80).ticketsSold(400L).date(LocalDate.parse("30-08-2019", dateFormatter)).time(LocalTime.parse( "15:00", timeFormatter)).description("description").build();
            Show show18 = Show.builder().id(18L).durationInMinutes(80).ticketsSold(100L).date(LocalDate.parse("30-03-2019", dateFormatter)).time(LocalTime.parse( "11:00", timeFormatter)).description("description").build();
            Show show19 = Show.builder().id(19L).durationInMinutes(80).ticketsSold(500L).date(LocalDate.parse("11-05-2019", dateFormatter)).time(LocalTime.parse( "19:30", timeFormatter)).description("description").build();
            Show show20 = Show.builder().id(20L).durationInMinutes(80).ticketsSold(500L).date(LocalDate.parse("06-01-2019", dateFormatter)).time(LocalTime.parse( "21:30", timeFormatter)).description("description").build();

            Show show21 = Show.builder().id(21L).durationInMinutes(190).ticketsSold(12000L).date(LocalDate.parse("05-02-2020", dateFormatter)).time(LocalTime.parse( "21:00", timeFormatter)).description("description").build();
            Show show22 = Show.builder().id(22L).durationInMinutes(200).ticketsSold(25000L).date(LocalDate.parse("19-03-2020", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show23 = Show.builder().id(23L).durationInMinutes(150).ticketsSold(1000L).date(LocalDate.parse("24-04-2020", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show24 = Show.builder().id(24L).durationInMinutes(160).ticketsSold(11000L).date(LocalDate.parse("22-08-2020", dateFormatter)).time(LocalTime.parse( "23:00", timeFormatter)).description("description").build();
            Show show25 = Show.builder().id(25L).durationInMinutes(130).ticketsSold(1800L).date(LocalDate.parse("23-11-2020", dateFormatter)).time(LocalTime.parse( "18:30", timeFormatter)).description("description").build();

            Show show26 = Show.builder().id(26L).durationInMinutes(180).ticketsSold(1000L).date(LocalDate.parse("09-10-2020", dateFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("description").build();
            Show show27 = Show.builder().id(27L).durationInMinutes(200).ticketsSold(2500L).date(LocalDate.parse("15-11-2020", dateFormatter)).time(LocalTime.parse( "20:30", timeFormatter)).description("description").build();
            Show show28 = Show.builder().id(28L).durationInMinutes(100).ticketsSold(1700L).date(LocalDate.parse("14-08-2020", dateFormatter)).time(LocalTime.parse( "17:30", timeFormatter)).description("description").build();
            Show show29 = Show.builder().id(29L).durationInMinutes(50).ticketsSold(100L).date(LocalDate.parse("07-07-2020", dateFormatter)).time(LocalTime.parse( "19:00", timeFormatter)).description("description").build();
            Show show30 = Show.builder().id(30L).durationInMinutes(60).ticketsSold(150L).date(LocalDate.parse("01-01-2020", dateFormatter)).time(LocalTime.parse( "19:30", timeFormatter)).description("description").build();

            Show show31 = Show.builder().id(31L).durationInMinutes(110).ticketsSold(1700L).date(LocalDate.parse("17-09-2020", dateFormatter)).time(LocalTime.parse( "21:00", timeFormatter)).description("description").build();
            Show show32 = Show.builder().id(32L).durationInMinutes(120).ticketsSold(550L).date(LocalDate.parse("19-02-2020", dateFormatter)).time(LocalTime.parse( "23:00", timeFormatter)).description("description").build();
            Show show33 = Show.builder().id(33L).durationInMinutes(40).ticketsSold(450L).date(LocalDate.parse("04-04-2020", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show34 = Show.builder().id(34L).durationInMinutes(130).ticketsSold(550L).date(LocalDate.parse("22-06-2020", dateFormatter)).time(LocalTime.parse( "14:30", timeFormatter)).description("description").build();
            Show show35 = Show.builder().id(35L).durationInMinutes(130).ticketsSold(50L).date(LocalDate.parse("29-12-2020", dateFormatter)).time(LocalTime.parse( "15:30", timeFormatter)).description("description").build();

            Show show36 = Show.builder().id(36L).durationInMinutes(100).ticketsSold(200L).date(LocalDate.parse("16-07-2020", dateFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("description").build();
            Show show37 = Show.builder().id(37L).durationInMinutes(110).ticketsSold(180L).date(LocalDate.parse("04-12-2020", dateFormatter)).time(LocalTime.parse( "12:00", timeFormatter)).description("description").build();
            Show show38 = Show.builder().id(38L).durationInMinutes(120).ticketsSold(120L).date(LocalDate.parse("03-11-2020", dateFormatter)).time(LocalTime.parse( "12:30", timeFormatter)).description("description").build();
            Show show39 = Show.builder().id(39L).durationInMinutes(90).ticketsSold(140L).date(LocalDate.parse("17-02-2020", dateFormatter)).time(LocalTime.parse( "13:00", timeFormatter)).time(LocalTime.parse( "10:30", timeFormatter)).description("description").build();
            Show show40 = Show.builder().id(40L).durationInMinutes(70).ticketsSold(190L).date(LocalDate.parse("23-02-2020", dateFormatter)).time(LocalTime.parse( "15:00", timeFormatter)).description("description").build();

            Show show41 = Show.builder().id(41L).durationInMinutes(70).ticketsSold(210L).date(LocalDate.parse("28-07-2020", dateFormatter)).time(LocalTime.parse( "17:30", timeFormatter)).description("description").build();
            Show show42 = Show.builder().id(42L).durationInMinutes(80).ticketsSold(440L).date(LocalDate.parse("30-08-2020", dateFormatter)).time(LocalTime.parse( "15:00", timeFormatter)).description("description").build();
            Show show43 = Show.builder().id(43L).durationInMinutes(80).ticketsSold(150L).date(LocalDate.parse("30-03-2020", dateFormatter)).time(LocalTime.parse( "11:00", timeFormatter)).description("description").build();
            Show show44 = Show.builder().id(44L).durationInMinutes(80).ticketsSold(590L).date(LocalDate.parse("11-05-2020", dateFormatter)).time(LocalTime.parse( "19:00", timeFormatter)).description("description").build();
            Show show45 = Show.builder().id(45L).durationInMinutes(80).ticketsSold(520L).date(LocalDate.parse("06-01-2020", dateFormatter)).time(LocalTime.parse( "21:30", timeFormatter)).description("description").build();

            Show show46 = Show.builder().id(46L).durationInMinutes(190).ticketsSold(1200L).date(LocalDate.parse("05-02-2019", dateFormatter)).time(LocalTime.parse( "21:00", timeFormatter)).description("description").build();
            Show show47 = Show.builder().id(47L).durationInMinutes(200).ticketsSold(2500L).date(LocalDate.parse("19-03-2019", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show48 = Show.builder().id(48L).durationInMinutes(150).ticketsSold(1800L).date(LocalDate.parse("24-04-2019", dateFormatter)).time(LocalTime.parse( "23:30", timeFormatter)).description("description").build();
            Show show49 = Show.builder().id(49L).durationInMinutes(160).ticketsSold(1100L).date(LocalDate.parse("22-08-2019", dateFormatter)).time(LocalTime.parse( "23:00", timeFormatter)).description("description").build();
            Show show50 = Show.builder().id(50L).durationInMinutes(130).ticketsSold(1800L).date(LocalDate.parse("23-11-2019", dateFormatter)).time(LocalTime.parse( "18:30", timeFormatter)).description("description").build();

            show1.setEvent(event1);
            show2.setEvent(event3);
            show3.setEvent(event4);
            show4.setEvent(event5);
            show5.setEvent(event6);
            show6.setEvent(event7);
            show7.setEvent(event9);
            show8.setEvent(event10);
            show9.setEvent(event13);
            show10.setEvent(event14);
            show11.setEvent(event15);
            show12.setEvent(event16);
            show13.setEvent(event19);
            show14.setEvent(event20);
            show15.setEvent(event4);
            show16.setEvent(event5);
            show17.setEvent(event7);
            show18.setEvent(event16);
            show19.setEvent(event15);
            show20.setEvent(event16);
            show21.setEvent(event17);
            show22.setEvent(event19);
            show23.setEvent(event20);
            show24.setEvent(event37);
            show25.setEvent(event33);
            show26.setEvent(event36);
            show27.setEvent(event34);
            show28.setEvent(event32);
            show29.setEvent(event27);
            show30.setEvent(event40);
            show31.setEvent(event22);
            show32.setEvent(event37);
            show33.setEvent(event21);
            show34.setEvent(event35);
            show35.setEvent(event34);
            show36.setEvent(event29);
            show37.setEvent(event26);
            show38.setEvent(event25);
            show39.setEvent(event21);
            show40.setEvent(event23);
            show41.setEvent(event36);
            show42.setEvent(event38);
            show43.setEvent(event39);
            show44.setEvent(event31);
            show45.setEvent(event30);
            show46.setEvent(event25);
            show47.setEvent(event26);
            show48.setEvent(event29);
            show49.setEvent(event28);
            show50.setEvent(event27);

            /*
             //Was giving Nested exception is java.lang.StackOverflowError (Don't know why)
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
            /*

            hall hall1 = hall.builder().id(1L).name("Hall1").build();
            hall hall2 = hall.builder().id(2L).name("Hall2").build();
            hall hall3 = hall.builder().id(3L).name("Hall3").build();
            hall hall4 = hall.builder().id(4L).name("Hall4").build();
            hall hall5 = hall.builder().id(5L).name("Hall5").build();
            hall hall6 = hall.builder().id(6L).name("Hall6").build();
            hall hall7 = hall.builder().id(7L).name("Hall7").build();
            hall hall8 = hall.builder().id(8L).name("Hall8").build();
            hall hall9 = hall.builder().id(9L).name("Hall9").build();
            hall hall10 = hall.builder().id(10L).name("Hall10").build();
            hall hall11 = hall.builder().id(11L).name("Hall11").build();
            hall hall12 = hall.builder().id(12L).name("Hall12").build();
            hall hall13 = hall.builder().id(13L).name("Hall13").build();
            hall hall14 = hall.builder().id(14L).name("Hall14").build();
            hall hall15 = hall.builder().id(15L).name("Hall15").build();

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
            show26.setHall(hall11);
            show27.setHall(hall13);
            show28.setHall(hall14);
            show29.setHall(hall9);
            show30.setHall(hall10);
            show31.setHall(hall12);
            show32.setHall(hall15);
            show33.setHall(hall15);
            show34.setHall(hall14);
            show35.setHall(hall2);
            show36.setHall(hall3);
            show37.setHall(hall5);
            show38.setHall(hall5);
            show39.setHall(hall5);
            show40.setHall(hall7);
            show41.setHall(hall1);
            show42.setHall(hall8);
            show43.setHall(hall8);
            show44.setHall(hall8);
            show45.setHall(hall3);
            show46.setHall(hall14);
            show47.setHall(hall15);
            show48.setHall(hall9);
            show49.setHall(hall10);
            show50.setHall(hall10);

            Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").postalCode("1090").street("Tendlergasse 12").build();
            Location location2 = Location.builder().id(2L).country("Austria").city("Vienna").postalCode("1220").street("Josef Baumann Gasse 8").build();
            Location location3 = Location.builder().id(3L).country("Austria").city("Vienna").postalCode("1010").street("Maria-Theresien-Platz").build();
            Location location4 = Location.builder().id(4L).country("Austria").city("Vienna").postalCode("1211").street("Siemensstraße 90").build();
            Location location5 = Location.builder().id(5L).country("Austria").city("Vienna").postalCode("1010").street("Burgring 7").build();
            Location location6 = Location.builder().id(6L).country("Austria").city("Vienna").postalCode("1030").street("Prinz Eugen-Straße 27").build();
            Location location7 = Location.builder().id(7L).country("Austria").city("Vienna").postalCode("1020").street("Oswald-Thomas-Platz 1").build();
            Location location8 = Location.builder().id(8L).country("Austria").city("Vienna").postalCode("1130").street("Schönbrunner Schloßstraße").build();
            Location location9 = Location.builder().id(9L).country("Austria").city("Vienna").postalCode("1060").street("Gumpendorfer Straße 142").build();
            Location location10 = Location.builder().id(10L).country("Austria").city("Vienna").postalCode("1050").street("Burggasse 121").build();

            hall1.setLocation(location1);
            hall2.setLocation(location2);
            hall3.setLocation(location3);
            hall4.setLocation(location1);
            hall5.setLocation(location7);
            hall6.setLocation(location6);
            hall7.setLocation(location7);
            hall8.setLocation(location8);
            hall9.setLocation(location9);
            hall10.setLocation(location4);
            hall11.setLocation(location5);
            hall12.setLocation(location9);
            hall13.setLocation(location8);
            hall14.setLocation(location9);
            hall15.setLocation(location3);

            artistRepository.saveAll(Arrays.asList(artist1, artist2, artist3, artist4, artist5, artist6, artist7, artist8, artist9, artist10, artist11, artist12, artist13));
            eventRepository.saveAll(Arrays.asList(event1, event2, event3, event4, event5, event6, event7, event8, event9, event10, event11, event12, event13, event14, event15, event16, event17, event18, event19, event20, event21, event22, event23, event24, event25, event26, event27, event28, event29, event30, event31, event32, event33, event34, event35, event35, event36, event37, event38, event39, event40));
            locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9, location10));
            hallRepository.saveAll(Arrays.asList(hall1, hall2, hall3, hall4, hall5, hall6, hall7, hall8, hall9, hall10, hall11, hall12, hall13, hall14, hall15));
            showRepository.saveAll(Arrays.asList(show1, show2, show3, show4, show5, show6, show7, show8, show9, show10, show11, show12, show13, show14, show15, show16, show17, show18, show19, show20, show21, show22, show23, show24, show25, show26, show27, show28, show29, show30, show31, show32, show33, show34, show35, show36, show37, show38, show39, show40, show41, show42, show43, show44, show45, show46, show47, show48, show49, show50));

        }*/
    }

}
