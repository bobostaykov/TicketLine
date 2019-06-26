package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generatePerformanceTestData")
@Component
public class HallPerformanceTestDataGenerator extends PerformanceTestDataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final HallRepository hallRepository;
    private final LocationRepository locationRepository;

    Faker faker = new Faker();

    public HallPerformanceTestDataGenerator(HallRepository hallRepository, LocationRepository locationRepository){
        this.hallRepository = hallRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void generate(){
        if(hallRepository.count() > 0){
            LOGGER.info("Halls already generated");
        }else {
            LOGGER.info("Generating {} halls", NUM_OF_HALLS);
            List<Hall> halls = new ArrayList<>();
            List<Seat> seats;
            List<Sector> sectors;
            for(Long id = 1L; id <= NUM_OF_HALLS; id++) {
                Boolean seatsOrSectors = id % 2 == 0;
                seats = seatsOrSectors ? new ArrayList<>() : null;
                sectors = seatsOrSectors ? null : new ArrayList<>();
                if (seatsOrSectors) {
                    for (Long seatId = 1L; seatId <= NUM_OF_SEATS_PER_HALL; seatId++)
                        seats.add(Seat.builder()
                            .priceCategory(seatId % 3 == 0 ? PriceCategory.CHEAP : seatId % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                            .seatNumber(customModInt(seatId, NUM_OF_SEAT_ROWS_PER_HALL))
                            .seatRow(Math.toIntExact((seatId-1) / NUM_OF_SEAT_ROWS_PER_HALL + 1))
                            .hall(hallRepository.getOne(customMod(id, NUM_OF_HALLS)))
                            .build());
                }
                else {
                    for (Long sectorId = 1L; sectorId <= NUM_OF_SECTORS_PER_HALL; sectorId++)
                        sectors.add(Sector.builder()
                            .priceCategory(sectorId % 3 == 0 ? PriceCategory.CHEAP : sectorId % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                            .sectorNumber(Math.toIntExact(customMod(sectorId, NUM_OF_SECTORS_PER_HALL)))
                            .maxCapacity((int)(Math.random() * 1000) + 100)
                            .hall(hallRepository.getOne(customMod(id, NUM_OF_HALLS)))
                            .build());
                }
                halls.add(Hall.builder()
                    .id(id)
                    .location(locationRepository.getOne(customMod(id, NUM_OF_LOCATIONS)))
                    .name(faker.bothify("HALL ????????"))
                    .seats(seatsOrSectors ? seats : null)
                    .sectors(seatsOrSectors ? null : sectors)
                    .build());
            }
            hallRepository.saveAll(halls);
        }
    }
}
