package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.PricePattern;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricePatternRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Profile("generateData")
@Component
public class PricePatternDataGenerator implements DataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final PricePatternRepository pricePatternRepository;

    public PricePatternDataGenerator(PricePatternRepository pricePatternRepository){
        this.pricePatternRepository = pricePatternRepository;
    }

    @Override
    public void generate(){
        if(pricePatternRepository.count() > 0){
            LOGGER.info("PricePattern already generated");
        }else {
            LOGGER.info("Generating pricePattern");
            PricePattern pricePattern = PricePattern.builder()
                .setId(1L)
                .setName("Test Price Pattern")
                .setPriceMapping(Map.ofEntries(
                    entry(PriceCategory.CHEAP, 13.0),
                    entry(PriceCategory.AVERAGE, 26.0),
                    entry(PriceCategory.EXPENSIVE, 52.0)))
                .createPricePattern();
            pricePatternRepository.save(pricePattern);
            PricePattern pricePattern2 = PricePattern.builder()
                .setId(1L)
                .setName("Test Price Pattern")
                .setPriceMapping(Map.ofEntries(
                    entry(PriceCategory.CHEAP, 9.0),
                    entry(PriceCategory.AVERAGE, 14.0),
                    entry(PriceCategory.EXPENSIVE, 19.0)))
                .createPricePattern();
            pricePatternRepository.save(pricePattern2);
        }
    }
}
