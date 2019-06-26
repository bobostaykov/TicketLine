package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
public class SeatDataGenerator implements DataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    private final static int numOfSeatsRowsPerHall = 5;
    private final static int numOfSeatsPerRow = 10;

    public SeatDataGenerator(SeatRepository seatRepository, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public void generate() {
        if (seatRepository.count() > 0) {
            LOGGER.info("Seats already generated");
        } else {
            LOGGER.info("Generating seats");
            List<Seat> seats = new ArrayList<>();
            Long numOfHalls = hallRepository.count();
            for (Long i = 1L; i <= numOfHalls; i+=2) {
                Hall hall = hallRepository.getOne(i);
                for (int seat = 1; seat <= numOfSeatsPerRow*numOfSeatsRowsPerHall; seat++) {
                    seats.add(Seat.builder()
                        .priceCategory(seat % 3 == 0 ? PriceCategory.CHEAP : seat % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                        .seatNumber(((seat-1) % numOfSeatsPerRow) + 1)
                        .seatRow((seat-1) / numOfSeatsPerRow + 1)
                        .hall(hall)
                        .build());
                }
            }
            seatRepository.saveAll(seats);
        }
    }

}
