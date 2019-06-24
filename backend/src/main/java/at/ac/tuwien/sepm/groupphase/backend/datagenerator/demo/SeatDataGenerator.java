package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
/*
    @Override
    public void generate(){
        if(seatRepository.count() > 0){
            LOGGER.info("Seats already generated");
        }else {
            LOGGER.info("Generating seats");
            Seat seat1 = Seat.builder().id(1L).seatNumber(22).seatRow(10).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(1L)).build();
            Seat seat2 = Seat.builder().id(2L).seatNumber(25).seatRow(12).priceCategory(PriceCategory.CHEAP).hall(hallRepository.getOne(2L)).build();
            Seat seat3 = Seat.builder().id(3L).seatNumber(26).seatRow(1).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(3L)).build();
            Seat seat4 = Seat.builder().id(4L).seatNumber(31).seatRow(22).priceCategory(PriceCategory.CHEAP).hall(hallRepository.getOne(1L)).build();
            Seat seat5 = Seat.builder().id(5L).seatNumber(2).seatRow(17).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(7L)).build();
            Seat seat6 = Seat.builder().id(6L).seatNumber(14).seatRow(18).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(6L)).build();
            Seat seat7 = Seat.builder().id(7L).seatNumber(5).seatRow(9).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(7L)).build();
            Seat seat8 = Seat.builder().id(8L).seatNumber(5).seatRow(2).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(8L)).build();
            Seat seat9 = Seat.builder().id(9L).seatNumber(25).seatRow(10).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(9L)).build();
            Seat seat10 = Seat.builder().id(10L).seatNumber(26).seatRow(10).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(9L)).build();

            seatRepository.saveAll(Arrays.asList(seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10));
        }
    }
*/

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
