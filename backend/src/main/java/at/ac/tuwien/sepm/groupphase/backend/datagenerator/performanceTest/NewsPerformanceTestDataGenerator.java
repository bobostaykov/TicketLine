package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generatePerformanceTestData")
@Component
public class NewsPerformanceTestDataGenerator extends PerformanceTestDataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String TEST_NEWS_IMAGE_ID = "1";

    private final NewsRepository newsRepository;
    private final Faker faker;

    public NewsPerformanceTestDataGenerator(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (newsRepository.count() > 0) {
            LOGGER.info("News already generated");
        } else {
            LOGGER.info("Generating {} news entries", NUM_OF_NEWS);
            for (int i = 0; i < NUM_OF_NEWS; i++) {
                News news = News.builder()
                    .title(faker.lorem().characters(30, 40))
                    .text(faker.lorem().paragraph(faker.number().numberBetween(5, 10)))
                    .image(TEST_NEWS_IMAGE_ID)
                    .publishedAt(
                        LocalDateTime.ofInstant(
                            faker.date()
                                .past(365 * 3, TimeUnit.DAYS).
                                toInstant(),
                            ZoneId.systemDefault()
                        ))
                    .build();
                LOGGER.debug("Saving news {}", news);
                newsRepository.save(news);
            }
        }
    }

}
