package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.configuration.PerformanceTestDataGeneratorConfiguration;
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
 * The classes manages the time and order of generating objects to fill the database for performance tests
 */
@Profile("generatePerformanceTestData")
@Component
public class PerformanceTestDataGeneratorManager implements ApplicationContextAware {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ApplicationContext context;

    PerformanceTestDataGeneratorManager(){
        context = new AnnotationConfigApplicationContext(PerformanceTestDataGeneratorConfiguration.class);
    }

    @PostConstruct
    private void generatePerformanceTestData() {
        LOGGER.info("---------- START DATA GENERATION ----------");
        context.getBean(NewsPerformanceTestDataGenerator.class).generate();
        context.getBean(ArtistPerformanceTestDataGenerator.class).generate();
        context.getBean(EventPerformanceTestDataGenerator.class).generate();
        context.getBean(LocationPerformanceTestDataGenerator.class).generate();
        context.getBean(HallPerformanceTestDataGenerator.class).generate();
        context.getBean(EventPerformanceTestDataGenerator.class).generate();
        context.getBean(ShowPerformanceTestDataGenerator.class).generate();
        context.getBean(CustomerPerformanceTestDataGenerator.class).generate();
        context.getBean(UserPerformanceTestDataGenerator.class).generate();
        context.getBean(TicketPerformanceTestDataGenerator.class).generate();
        LOGGER.info("--------- DATA GENERATION COMPLETE --------");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
