package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class EventDataGenerator implements DataGenerator{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    public EventDataGenerator(EventRepository eventRepository, ArtistRepository artistRepository){
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public void generate(){
        if(eventRepository.count() > 0){
            LOGGER.info("Artists already generated");
        }else {
            LOGGER.info("Generating artists");
            Event event1 = Event.builder().id(1L).name("Balloons N Ribbons").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(1L)).description("description").content("content").build();
            Event event2 = Event.builder().id(2L).name("MemoriesMade").eventType(EventType.MOVIE).artist(null).description("COMING SOON").content("content").build();
            Event event3 = Event.builder().id(3L).name("Lucky Charms").eventType(EventType.CONCERT).artist(artistRepository.getOne(1L)).description("description").content("content").build();
            Event event4 = Event.builder().id(4L).name("PartyPies").eventType(EventType.MUSICAL).artist(artistRepository.getOne(1L)).description("description").content("content").build();
            Event event5 = Event.builder().id(5L).name("Dreams Come True").eventType(EventType.OPERA).artist(artistRepository.getOne(1L)).description("description").content(null).build();

            Event event6 = Event.builder().id(6L).name("Superathon 2019").eventType(EventType.SPORT).artist(artistRepository.getOne(2L)).description("description").content("content").build();
            Event event7 = Event.builder().id(7L).name("Back in time 20'").eventType(EventType.CONCERT).artist(artistRepository.getOne(2L)).description("description").content(null).build();
            Event event8 = Event.builder().id(8L).name("HouseParty").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content(null).build();
            Event event9 = Event.builder().id(9L).name("Music Festival 21'").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(2L)).description("description").content("content").build();
            Event event10 = Event.builder().id(10L).name("Fate & Fiesta").eventType(EventType.THEATRE).artist(artistRepository.getOne(2L)).description("description").content(null).build();

            Event event11 = Event.builder().id(11L).name("Galla Evening For Many").eventType(EventType.CONCERT).artist(null).description("COMING SOON").content(null).build();
            Event event12 = Event.builder().id(12L).name("Early Halloween").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content("content").build();
            Event event13 = Event.builder().id(13L).name("Welcome to Valhalla").eventType(EventType.THEATRE).artist(artistRepository.getOne(3L)).description("description").content(null).build();
            Event event14 = Event.builder().id(14L).name("Animecon").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(3L)).description("description").content(null).build();
            Event event15 = Event.builder().id(15L).name("Superbowl").eventType(EventType.SPORT).artist(artistRepository.getOne(4L)).description("description").content(null).build();

            Event event16 = Event.builder().id(16L).name("Carnival of the 100 seasons").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(4L)).description("description").content("content").build();
            Event event17 = Event.builder().id(17L).name("Fest Frost Fast Fist").eventType(EventType.CONCERT).artist(artistRepository.getOne(4L)).description("description").content(null).build();
            Event event18 = Event.builder().id(18L).name("LetMeFixYa").eventType(EventType.MOVIE).artist(null).description("COMING SOON").content(null).build();
            Event event19 = Event.builder().id(19L).name("Quite a Quiet Quiz").eventType(EventType.OPERA).artist(artistRepository.getOne(5L)).description("description").content("content").build();
            Event event20 = Event.builder().id(20L).name("Theatre Seven Dwarfs").eventType(EventType.THEATRE).artist(artistRepository.getOne(6L)).description("description").content(null).build();

            Event event21 = Event.builder().id(21L).name("Guns and Ships").eventType(EventType.OPERA).artist(artistRepository.getOne(8L)).description("description").content("content").build();
            Event event22 = Event.builder().id(22L).name("Bad Girls Play With Fire").eventType(EventType.OPERA).artist(null).description("COMING SOON").content("content").build();
            Event event23 = Event.builder().id(23L).name("How to Party").eventType(EventType.CONCERT).artist(artistRepository.getOne(8L)).description("description").content("content").build();
            Event event24 = Event.builder().id(24L).name("Magiciancon 2019").eventType(EventType.MUSICAL).artist(artistRepository.getOne(8L)).description("description").content("content").build();
            Event event25 = Event.builder().id(25L).name("Zombie Mashup").eventType(EventType.OPERA).artist(artistRepository.getOne(8L)).description("description").content(null).build();

            Event event26 = Event.builder().id(26L).name("Marathon For The Youngs").eventType(EventType.SPORT).artist(artistRepository.getOne(9L)).description("description").content("content").build();
            Event event27 = Event.builder().id(27L).name("HighSchool Reopening'").eventType(EventType.CONCERT).artist(artistRepository.getOne(10L)).description("description").content(null).build();
            Event event28 = Event.builder().id(28L).name("Dirty Diana").eventType(EventType.MUSICAL).artist(null).description("COMING SOON").content(null).build();
            Event event29 = Event.builder().id(29L).name("Coachella 2020").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(12L)).description("description").content("content").build();
            Event event30 = Event.builder().id(30L).name("Some Old Time Roads").eventType(EventType.THEATRE).artist(artistRepository.getOne(13L)).description("description").content(null).build();

            Event event31 = Event.builder().id(31L).name("Yoga for 1000 people").eventType(EventType.SPORT).artist(null).description("COMING SOON").content(null).build();
            Event event32 = Event.builder().id(32L).name("Cringe Party 2020").eventType(EventType.FESTIVAL).artist(null).description("COMING SOON").content("content").build();
            Event event33 = Event.builder().id(33L).name("When God meets Groundmother").eventType(EventType.MOVIE).artist(artistRepository.getOne(11L)).description("description").content(null).build();
            Event event34 = Event.builder().id(34L).name("Doggy with no style").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(12L)).description("description").content(null).build();
            Event event35 = Event.builder().id(35L).name("Uncle Benny").eventType(EventType.MOVIE).artist(artistRepository.getOne(13L)).description("description").content(null).build();

            Event event36 = Event.builder().id(36L).name("Habibis United").eventType(EventType.FESTIVAL).artist(artistRepository.getOne(13L)).description("description").content("content").build();
            Event event37 = Event.builder().id(37L).name("Tomato Sause").eventType(EventType.THEATRE).artist(artistRepository.getOne(13L)).description("description").content(null).build();
            Event event38 = Event.builder().id(38L).name("Bobby Hobby").eventType(EventType.THEATRE).artist(artistRepository.getOne(9L)).description("description").content(null).build();
            Event event39 = Event.builder().id(39L).name("Best Of Opera").eventType(EventType.OPERA).artist(artistRepository.getOne(11L)).description("description").content("content").build();
            Event event40 = Event.builder().id(40L).name("Mozart Regrets Himself").eventType(EventType.CONCERT).artist(artistRepository.getOne(10L)).description("description").content(null).build();

            eventRepository.saveAll(Arrays.asList(event1, event2, event3, event4, event5, event6, event7, event8, event9, event10,
                event11, event12, event13, event14, event15, event16, event17, event18, event19, event20, event21, event22, event23,
                event24, event25, event26, event27, event28, event29, event30, event31, event32, event33, event34, event35, event35,
                event36, event37, event38, event39, event40));
        }
    }
}
