package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

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
 * CLASS NO LONGER USED
 * The classes manages the time and order of generating objects to fill the database
 */
@Profile("generateData")
@Component
public class DataGeneratorManager implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGeneratorManager.class);
    private ApplicationContext context;

    DataGeneratorManager(){
        context = new AnnotationConfigApplicationContext(DataGeneratorConfiguration.class);
    }

    @PostConstruct
    private void generateData() {
        LOGGER.info("---------- START DATA GENERATION ----------");
        //context.getBean(<Object>DataGenerator.class).generate<Objects>();
        LOGGER.info("--------- DATA GENERATION COMPLETE --------");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
