package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class MessageDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDataGenerator.class);
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 25;

    private final MessageRepository messageRepository;
    private final Faker faker;

    public MessageDataGenerator(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateMessage() {
        if (messageRepository.count() > 0) {
            LOGGER.info("message already generated");
        } else {
            LOGGER.info("generating {} message entries", NUMBER_OF_NEWS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                Message message = Message.builder()
                    .title(faker.lorem().characters(30, 40))
                    .text(faker.lorem().paragraph(faker.number().numberBetween(5, 10)))
                    .publishedAt(
                        LocalDateTime.ofInstant(
                            faker.date()
                                .past(365 * 3, TimeUnit.DAYS).
                                toInstant(),
                            ZoneId.systemDefault()
                        ))
                    .build();
                LOGGER.debug("saving message {}", message);
                messageRepository.save(message);
            }
        }
    }

}
