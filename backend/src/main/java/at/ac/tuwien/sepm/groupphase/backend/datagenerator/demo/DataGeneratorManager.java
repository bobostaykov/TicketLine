package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.configuration.DataGeneratorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The classes manages the time and order of generating objects to fill the database
 */
@Profile("generateData")
@Component
public class DataGeneratorManager implements ApplicationContextAware {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ApplicationContext context;

    DataGeneratorManager(){
        context = new AnnotationConfigApplicationContext(DataGeneratorConfiguration.class);
    }

    @PostConstruct
    private void generateData() {
        LOGGER.info("---------- START DATA GENERATION ----------");
        context.getBean(NewsDataGenerator.class).generate();
        context.getBean(ArtistDataGenerator.class).generate();
        context.getBean(EventDataGenerator.class).generate();
        context.getBean(LocationDataGenerator.class).generate();
        context.getBean(HallDataGenerator.class).generate();
        context.getBean(PricePatternDataGenerator.class).generate();
        context.getBean(EventDataGenerator.class).generate();
        context.getBean(ShowDataGenerator.class).generate();
        context.getBean(CustomerDataGenerator.class).generate();
        context.getBean(UserDataGenerator.class).generate();
        context.getBean(SeatDataGenerator.class).generate();
        context.getBean(SectorDataGenerator.class).generate();
        context.getBean(TicketDataGenerator.class).generate();
        LOGGER.info("--------- DATA GENERATION COMPLETE --------");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
