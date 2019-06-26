package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.entity.PricePattern;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricePatternRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Profile("generateData")
@Component
public class ShowDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final PricePatternRepository pricePatternRepository;

    public ShowDataGenerator(ShowRepository showRepository, EventRepository eventRepository, HallRepository hallRepository,
                             PricePatternRepository pricePatternRepository){
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.pricePatternRepository = pricePatternRepository;
        dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }


    @Override
    public void generate(){

        if(showRepository.count() > 0){
            LOGGER.info("Shows already generated");
        }else {
            LOGGER.info("Generating shows");
            PricePattern pricePattern = pricePatternRepository.getOne(1L);

            Show show1 = Show.builder().id(1L).event(eventRepository.getOne(1L)).hall(hallRepository.getOne(1L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("09-10-2020", dateFormatter)).time(LocalTime.parse("10:30", timeFormatter)).description("description").build();
            Show show2 = Show.builder().id(2L).event(eventRepository.getOne(3L)).hall(hallRepository.getOne(2L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("15-11-2020", dateFormatter)).time(LocalTime.parse("20:30", timeFormatter)).description("description").build();
            Show show3 = Show.builder().id(3L).event(eventRepository.getOne(4L)).hall(hallRepository.getOne(3L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("14-08-2020", dateFormatter)).time(LocalTime.parse( "17:30", timeFormatter)).description("description").build();
            Show show4 = Show.builder().id(4L).event(eventRepository.getOne(5L)).hall(hallRepository.getOne(4L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("07-07-2020", dateFormatter)).time(LocalTime.parse( "19:00", timeFormatter)).description("description").build();
            Show show5 = Show.builder().id(5L).event(eventRepository.getOne(6L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("01-01-2020", dateFormatter)).time(LocalTime.parse( "19:30", timeFormatter)).description("description").build();

            Show show6 = Show.builder().id(6L).event(eventRepository.getOne(7L)).hall(hallRepository.getOne(6L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("17-09-2029", dateFormatter)).time(LocalTime.parse( "21:00",timeFormatter)).description("description").build();
            Show show7 = Show.builder().id(7L).event(eventRepository.getOne(9L)).hall(hallRepository.getOne(7L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("19-02-2020", dateFormatter)).time(LocalTime.parse( "23:00",timeFormatter)).description("description").build();
            Show show8 = Show.builder().id(8L).event(eventRepository.getOne(10L)).hall(hallRepository.getOne(8L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("04-04-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show9 = Show.builder().id(9L).event(eventRepository.getOne(13L)).hall(hallRepository.getOne(9L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("22-06-2020", dateFormatter)).time(LocalTime.parse( "14:30",timeFormatter)).description("description").build();
            Show show10 = Show.builder().id(10L).event(eventRepository.getOne(14L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("29-12-2020", dateFormatter)).time(LocalTime.parse( "15:30",timeFormatter)).description("description").build();

            Show show11 = Show.builder().id(11L).event(eventRepository.getOne(15L)).hall(hallRepository.getOne(1L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("16-07-2020", dateFormatter)).time(LocalTime.parse( "10:30",timeFormatter)).description("description").build();
            Show show12 = Show.builder().id(12L).event(eventRepository.getOne(16L)).hall(hallRepository.getOne(1L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("04-12-2020", dateFormatter)).time(LocalTime.parse( "12:00",timeFormatter)).description("description").build();
            Show show13 = Show.builder().id(13L).event(eventRepository.getOne(19L)).hall(hallRepository.getOne(2L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("03-11-2020", dateFormatter)).time(LocalTime.parse( "12:30",timeFormatter)).description("description").build();
            Show show14 = Show.builder().id(14L).event(eventRepository.getOne(20L)).hall(hallRepository.getOne(3L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("17-02-2020", dateFormatter)).time(LocalTime.parse( "13:00",timeFormatter)).description("description").build();
            Show show15 = Show.builder().id(15L).event(eventRepository.getOne(4L)).hall(hallRepository.getOne(4L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("23-02-2020", dateFormatter)).time(LocalTime.parse( "15:00",timeFormatter)).description("description").build();

            Show show16 = Show.builder().id(16L).event(eventRepository.getOne(5L)).hall(hallRepository.getOne(4L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("28-07-2020", dateFormatter)).time(LocalTime.parse( "17:30",timeFormatter)).description("description").build();
            Show show17 = Show.builder().id(17L).event(eventRepository.getOne(7L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("30-08-2020", dateFormatter)).time(LocalTime.parse( "15:00",timeFormatter)).description("description").build();
            Show show18 = Show.builder().id(18L).event(eventRepository.getOne(16L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("30-03-2020", dateFormatter)).time(LocalTime.parse( "11:00",timeFormatter)).description("description").build();
            Show show19 = Show.builder().id(19L).event(eventRepository.getOne(15L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("11-05-2020", dateFormatter)).time(LocalTime.parse( "19:00",timeFormatter)).description("description").build();
            Show show20 = Show.builder().id(20L).event(eventRepository.getOne(16L)).hall(hallRepository.getOne(6L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("06-01-2020", dateFormatter)).time(LocalTime.parse( "21:30",timeFormatter)).description("description").build();

            Show show21 = Show.builder().id(21L).event(eventRepository.getOne(17L)).hall(hallRepository.getOne(7L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("05-02-2020", dateFormatter)).time(LocalTime.parse( "21:00",timeFormatter)).description("description").build();
            Show show22 = Show.builder().id(22L).event(eventRepository.getOne(19L)).hall(hallRepository.getOne(8L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("19-03-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show23 = Show.builder().id(23L).event(eventRepository.getOne(20L)).hall(hallRepository.getOne(9L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("24-04-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show24 = Show.builder().id(24L).event(eventRepository.getOne(37L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("22-08-2020", dateFormatter)).time(LocalTime.parse( "23:00",timeFormatter)).description("description").build();
            Show show25 = Show.builder().id(25L).event(eventRepository.getOne(33L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("23-11-2020", dateFormatter)).time(LocalTime.parse( "18:30",timeFormatter)).description("description").build();

            Show show26 = Show.builder().id(26L).event(eventRepository.getOne(36L)).hall(hallRepository.getOne(11L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("09-10-2020", dateFormatter)).time(LocalTime.parse( "10:30",timeFormatter)).description("description").build();
            Show show27 = Show.builder().id(27L).event(eventRepository.getOne(34L)).hall(hallRepository.getOne(13L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("15-11-2020", dateFormatter)).time(LocalTime.parse( "20:30",timeFormatter)).description("description").build();
            Show show28 = Show.builder().id(28L).event(eventRepository.getOne(32L)).hall(hallRepository.getOne(14L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("14-08-2020", dateFormatter)).time(LocalTime.parse( "17:30",timeFormatter)).description("description").build();
            Show show29 = Show.builder().id(29L).event(eventRepository.getOne(27L)).hall(hallRepository.getOne(9L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("07-07-2020", dateFormatter)).time(LocalTime.parse( "19:00",timeFormatter)).description("description").build();
            Show show30 = Show.builder().id(30L).event(eventRepository.getOne(40L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("01-01-2020", dateFormatter)).time(LocalTime.parse( "19:30",timeFormatter)).description("description").build();

            Show show31 = Show.builder().id(31L).event(eventRepository.getOne(22L)).hall(hallRepository.getOne(12L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("17-09-2020", dateFormatter)).time(LocalTime.parse( "21:00",timeFormatter)).description("description").build();
            Show show32 = Show.builder().id(32L).event(eventRepository.getOne(37L)).hall(hallRepository.getOne(15L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("19-02-2020", dateFormatter)).time(LocalTime.parse( "23:00",timeFormatter)).description("description").build();
            Show show33 = Show.builder().id(33L).event(eventRepository.getOne(21L)).hall(hallRepository.getOne(15L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("04-04-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show34 = Show.builder().id(34L).event(eventRepository.getOne(35L)).hall(hallRepository.getOne(14L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("22-06-2020", dateFormatter)).time(LocalTime.parse( "14:30",timeFormatter)).description("description").build();
            Show show35 = Show.builder().id(35L).event(eventRepository.getOne(34L)).hall(hallRepository.getOne(2L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("29-12-2020", dateFormatter)).time(LocalTime.parse( "15:30",timeFormatter)).description("description").build();

            Show show36 = Show.builder().id(36L).event(eventRepository.getOne(29L)).hall(hallRepository.getOne(3L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("16-07-2020", dateFormatter)).time(LocalTime.parse( "10:30",timeFormatter)).description("description").build();
            Show show37 = Show.builder().id(37L).event(eventRepository.getOne(26L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("04-12-2020", dateFormatter)).time(LocalTime.parse( "12:00",timeFormatter)).description("description").build();
            Show show38 = Show.builder().id(38L).event(eventRepository.getOne(25L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("03-11-2020", dateFormatter)).time(LocalTime.parse( "12:30",timeFormatter)).description("description").build();
            Show show39 = Show.builder().id(39L).event(eventRepository.getOne(21L)).hall(hallRepository.getOne(5L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("17-02-2020", dateFormatter)).time(LocalTime.parse( "13:00",timeFormatter)).description("description").build();
            Show show40 = Show.builder().id(40L).event(eventRepository.getOne(23L)).hall(hallRepository.getOne(7L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("23-02-2020", dateFormatter)).time(LocalTime.parse( "15:00",timeFormatter)).description("description").build();

            Show show41 = Show.builder().id(41L).event(eventRepository.getOne(36L)).hall(hallRepository.getOne(1L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("28-07-2020", dateFormatter)).time(LocalTime.parse( "17:30",timeFormatter)).description("description").build();
            Show show42 = Show.builder().id(42L).event(eventRepository.getOne(38L)).hall(hallRepository.getOne(8L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("30-08-2020", dateFormatter)).time(LocalTime.parse( "15:00",timeFormatter)).description("description").build();
            Show show43 = Show.builder().id(43L).event(eventRepository.getOne(39L)).hall(hallRepository.getOne(8L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("30-03-2020", dateFormatter)).time(LocalTime.parse( "11:00",timeFormatter)).description("description").build();
            Show show44 = Show.builder().id(44L).event(eventRepository.getOne(31L)).hall(hallRepository.getOne(8L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("11-05-2020", dateFormatter)).time(LocalTime.parse( "19:00",timeFormatter)).description("description").build();
            Show show45 = Show.builder().id(45L).event(eventRepository.getOne(30L)).hall(hallRepository.getOne(3L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("06-01-2020", dateFormatter)).time(LocalTime.parse( "21:30",timeFormatter)).description("description").build();

            Show show46 = Show.builder().id(46L).event(eventRepository.getOne(25L)).hall(hallRepository.getOne(14L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("05-02-2020", dateFormatter)).time(LocalTime.parse( "21:00",timeFormatter)).description("description").build();
            Show show47 = Show.builder().id(47L).event(eventRepository.getOne(26L)).hall(hallRepository.getOne(15L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("19-03-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show48 = Show.builder().id(48L).event(eventRepository.getOne(29L)).hall(hallRepository.getOne(9L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("24-04-2020", dateFormatter)).time(LocalTime.parse( "23:30",timeFormatter)).description("description").build();
            Show show49 = Show.builder().id(49L).event(eventRepository.getOne(28L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("22-08-2020", dateFormatter)).time(LocalTime.parse( "23:00",timeFormatter)).description("description").build();
            Show show50 = Show.builder().id(50L).event(eventRepository.getOne(27L)).hall(hallRepository.getOne(10L)).ticketsSold(0L).pricePattern(pricePattern).date(LocalDate.parse("23-11-2020", dateFormatter)).time(LocalTime.parse( "18:30",timeFormatter)).description("description").build();

            showRepository.saveAll(Arrays.asList(show1, show2, show3, show4, show5, show6, show7, show8, show9, show10,
                show11, show12, show13, show14, show15, show16, show17, show18, show19, show20, show21, show22, show23,
                show24, show25, show26, show27, show28, show29, show30, show31, show32, show33, show34, show35, show36,
                show37, show38, show39, show40, show41, show42, show43, show44, show45, show46, show47, show48, show49, show50));

        }
    }
}
