package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class HallDataGenerator implements DataGenerator{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final HallRepository hallRepository;
    private final LocationRepository locationRepository;

    public HallDataGenerator(HallRepository hallRepository, LocationRepository locationRepository){
        this.hallRepository = hallRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void generate(){
        if(hallRepository.count() > 0){
            LOGGER.info("Halls already generated");
        }else {
            LOGGER.info("Generating halls");
            Hall hall1 = Hall.builder().id(1L).name("Hall1").location(locationRepository.getOne(1L)).build();
            Hall hall2 = Hall.builder().id(2L).name("Hall2").location(locationRepository.getOne(2L)).build();
            Hall hall3 = Hall.builder().id(3L).name("Hall3").location(locationRepository.getOne(3L)).build();
            Hall hall4 = Hall.builder().id(4L).name("Hall4").location(locationRepository.getOne(1L)).build();
            Hall hall5 = Hall.builder().id(5L).name("Hall5").location(locationRepository.getOne(7L)).build();
            Hall hall6 = Hall.builder().id(6L).name("Hall6").location(locationRepository.getOne(6L)).build();
            Hall hall7 = Hall.builder().id(7L).name("Hall7").location(locationRepository.getOne(7L)).build();
            Hall hall8 = Hall.builder().id(8L).name("Hall8").location(locationRepository.getOne(8L)).build();
            Hall hall9 = Hall.builder().id(9L).name("Hall9").location(locationRepository.getOne(9L)).build();
            Hall hall10 = Hall.builder().id(10L).name("Hall10").location(locationRepository.getOne(4L)).build();
            Hall hall11 = Hall.builder().id(11L).name("Hall11").location(locationRepository.getOne(5L)).build();
            Hall hall12 = Hall.builder().id(12L).name("Hall12").location(locationRepository.getOne(9L)).build();
            Hall hall13 = Hall.builder().id(13L).name("Hall13").location(locationRepository.getOne(8L)).build();
            Hall hall14 = Hall.builder().id(14L).name("Hall14").location(locationRepository.getOne(9L)).build();
            Hall hall15 = Hall.builder().id(15L).name("Hall15").location(locationRepository.getOne(3L)).build();

            hallRepository.saveAll(Arrays.asList(hall1, hall2, hall3, hall4, hall5, hall6,
                hall7, hall8, hall9, hall10, hall11, hall12, hall13, hall14, hall15));
        }
    }
}
