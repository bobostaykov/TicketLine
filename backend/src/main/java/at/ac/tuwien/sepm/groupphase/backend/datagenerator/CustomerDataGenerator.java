package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Arrays;

@Profile("generateData")
@Component
public class CustomerDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final CustomerRepository customerRepository;

    public CustomerDataGenerator(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void generate(){
        if(customerRepository.count() > 0){
            LOGGER.info("Customers already generated");
        }else {
            LOGGER.info("Generating customers");
            Long id = 1L, cn = 1L, sn = 1L;
            Customer customer1 = Customer.builder().id(id++).name(faker.name().lastName()).firstname(faker.name().firstName()).email(faker.bothify("????##@gmail.com")).birthday(faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()).build();
            Customer customer2 = Customer.builder().id(id++).name(faker.name().lastName()).firstname(faker.name().firstName()).email(faker.bothify("????##@gmail.com")).birthday(faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()).build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2));
        }
    }
}
