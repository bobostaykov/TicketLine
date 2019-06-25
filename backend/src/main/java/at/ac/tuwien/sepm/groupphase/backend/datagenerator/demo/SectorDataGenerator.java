package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
public class SectorDataGenerator implements DataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final SectorRepository sectorRepository;
    private final HallRepository hallRepository;

    private final static int numOfSectors = 5;

    public SectorDataGenerator(HallRepository hallRepository, SectorRepository sectorRepository) {
        this.hallRepository = hallRepository;
        this.sectorRepository = sectorRepository;
    }

    @Override
    public void generate() {
        if (sectorRepository.count() > 0) {
            LOGGER.info("Sectors already generated");
        } else {
            LOGGER.info("Generating sectors");
            List<Sector> sectors = new ArrayList<>();
            Long numOfHalls = hallRepository.count();
            for (Long i = 2L; i <= numOfHalls; i+=2) {
                Hall hall = hallRepository.getOne(i);
                for (int sector = 1; sector <= numOfSectors; sector++) {
                    sectors.add(Sector.builder()
                        .priceCategory(sector % 3 == 0 ? PriceCategory.CHEAP : sector % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                        .sectorNumber(sector)
                        .hall(hall)
                        .maxCapacity((int)(Math.random() * 1000) + 100)
                        .build());
                }
            }
            sectorRepository.saveAll(sectors);
        }
    }

}
