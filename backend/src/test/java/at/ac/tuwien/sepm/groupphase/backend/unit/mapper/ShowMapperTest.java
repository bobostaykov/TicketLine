package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalTime;

import static at.ac.tuwien.sepm.groupphase.backend.datatype.EventType.MOVIE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ShowMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class ShowMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ShowMapper showMapper;

    private static final long ID = 1L;
    private static final Event EVENT = Event.builder()
        .id(2L)
        .name("Event")
        .eventType(MOVIE)
        .content("content")
        .description("event description")
        .build();
    private static final Location LOCATION = Location.builder()
        .id(3L).country("Country")
        .city("City")
        .street("Street")
        .postalCode("0000")
        .description("location description")
        .build();
    private static final Hall HALL = Hall.builder()
        .id(4L)
        .name("hall")
        .location(LOCATION)
        .build();
    private static final Long TICKETSSOLD = 25L;
    private static final String DESCRIPTION = "show description";
    private static final LocalDate DATE =
        LocalDate.of(2016, 1, 1);
    private static final LocalTime TIME =
        LocalTime.of(1,1);

    @Test
    public void shouldMapShowToShowDTO(){
        Show show = Show.builder()
            .id(ID)
            .event(EVENT)
            .hall(HALL)
            .date(DATE)
            .time(TIME)
            .ticketsSold(TICKETSSOLD)
            .description(DESCRIPTION)
            .build();

        ShowDTO showDTO = showMapper.showToShowDTO(show);

        assertThat(showDTO).isNotNull();
        assertThat(showDTO.getId()).isEqualTo(ID);
        assertThat(showDTO.getDate()).isEqualTo(DATE);
        assertThat(showDTO.getTime()).isEqualTo(TIME);
        assertThat(showDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(showDTO.getTicketsSold()).isEqualTo(TICKETSSOLD);
        assertThat(showDTO.getEvent().getId()).isEqualTo(2L);
        assertThat(showDTO.getEvent().getName()).isEqualTo("Event");
        assertThat(showDTO.getEvent().getEventType()).isEqualTo(MOVIE);
        assertThat(showDTO.getEvent().getContent()).isEqualTo("content");
        assertThat(showDTO.getEvent().getDescription()).isEqualTo("event description");
        assertThat(showDTO.getEvent().getArtist()).isNull();

        assertThat(showDTO.getHall().getId()).isEqualTo(4L);
        assertThat(showDTO.getHall().getName()).isEqualTo("hall");

        assertThat(showDTO.getHall().getLocation().getId()).isEqualTo(LOCATION.getId());
        assertThat(showDTO.getHall().getLocation().getCountry()).isEqualTo(LOCATION.getCountry());
        assertThat(showDTO.getHall().getLocation().getCity()).isEqualTo(LOCATION.getCity());
        assertThat(showDTO.getHall().getLocation().getStreet()).isEqualTo(LOCATION.getStreet());
        assertThat(showDTO.getHall().getLocation().getPostalCode()).isEqualTo(LOCATION.getPostalCode());
        assertThat(showDTO.getHall().getLocation().getDescription()).isEqualTo(LOCATION.getDescription());
    }

    @Test
    public void shouldMapShowDTOToShow(){
        EventDTO eventDTO = EventDTO.builder()
            .id(2L)
            .name("Event")
            .eventType(MOVIE)
            .content("content")
            .description("event description")
            .build();
        LocationDTO locationDTO = LocationDTO.builder()
            .id(3L)
            .country("Country")
            .city("City")
            .street("Street")
            .postalCode("0000")
            .description("location description")
            .build();
        HallDTO hallDTO = HallDTO.builder()
            .id(4L)
            .name("hall")
            .location(locationDTO)
            .build();
        ShowDTO showDTO = ShowDTO.builder()
            .id(ID)
            .event(eventDTO)
            .hall(hallDTO)
            .date(DATE)
            .time(TIME)
            .ticketsSold(TICKETSSOLD)
            .description(DESCRIPTION)
            .build();

        Show show = showMapper.showDTOToShow(showDTO);

        assertThat(show).isNotNull();
        assertThat(show.getId()).isEqualTo(ID);
        assertThat(show.getTicketsSold()).isEqualTo(TICKETSSOLD);
        assertThat(show.getDate()).isEqualTo(DATE);
        assertThat(show.getTime()).isEqualTo(TIME);
        assertThat(show.getDescription()).isEqualTo(DESCRIPTION);

        assertThat(show.getEvent().getId()).isEqualTo(2L);
        assertThat(show.getEvent().getName()).isEqualTo("Event");
        assertThat(show.getEvent().getEventType()).isEqualTo(MOVIE);
        assertThat(show.getEvent().getContent()).isEqualTo("content");
        assertThat(show.getEvent().getDescription()).isEqualTo("event description");
        assertThat(show.getEvent().getArtist()).isNull();

        assertThat(show.getHall().getId()).isEqualTo(4L);
        assertThat(show.getHall().getName()).isEqualTo("hall");

        assertThat(show.getHall().getLocation().getId()).isEqualTo(locationDTO.getId());
        assertThat(show.getHall().getLocation().getCountry()).isEqualTo(locationDTO.getCountry());
        assertThat(show.getHall().getLocation().getCity()).isEqualTo(locationDTO.getCity());
        assertThat(show.getHall().getLocation().getStreet()).isEqualTo(locationDTO.getStreet());
        assertThat(show.getHall().getLocation().getPostalCode()).isEqualTo(locationDTO.getPostalCode());
        assertThat(show.getHall().getLocation().getDescription()).isEqualTo(locationDTO.getDescription());
    }
}
