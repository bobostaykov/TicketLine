package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class SeatDataGenerator implements DataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    public SeatDataGenerator(SeatRepository seatRepository, HallRepository hallRepository){
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

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
}
