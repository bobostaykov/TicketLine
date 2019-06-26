package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Profile("generatePerformanceTestData")
@Component
public class CustomerPerformanceTestDataGenerator extends PerformanceTestDataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    Faker faker = new Faker();

    private final CustomerRepository customerRepository;

    public CustomerPerformanceTestDataGenerator(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void generate(){
        if(customerRepository.count() > 0){
            LOGGER.info("Customers already generated");
        }else {
            LOGGER.info("Generating {} customers", NUM_OF_CUSTOMERS);
            List<Customer> customers = new ArrayList<>();
            for(Long id = 1L; id <= NUM_OF_CUSTOMERS; id++) {
                customers.add(Customer.builder()
                    .id(id)
                    .name(faker.name().lastName())
                    .firstname(faker.name().firstName())
                    .email(faker.bothify("????##@gmail.com"))
                    .birthday(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .build());
            }
            customerRepository.saveAll(customers);
        }
    }
}