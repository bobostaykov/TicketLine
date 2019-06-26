package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.DBFileRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class NewsDataGenerator implements DataGenerator{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 25;
    private static final String TEST_NEWS_IMAGE_ID = "0";
    private static final String DEMO_NEWS_TITLES[] = {
        "TU Wien Bibliothek takes over the coordination of the ORCID Austria",
        "Upcoming events of Forum TU Vision 2025+",
        "EU Mobility Funding for Researchers",
        "Light from exotic particle states",
        "The Random Anti-Laser",
        "Austrian Microfluidics Initiative: Bridging gaps between product and research",
        "How to Freeze Heat Conduction",
        "Superconduction: Why does it have to be so cold?"
    };
    private static final String DEMO_NEWS_TEXTS[] = {
        "Identifiers such as the ORCID iD facilitate the allocation and searchability of research results. Researchers of the TU Wien are now optimally supported by a membership of the TU Wien Bibliothek in ORCID.",
        "Engineering Science with Impact\n" +
            "Lecture by Johann Kollegger, head of the research programme for Reinforced Concrete and Solid Construction\n" +
            "In the summer term, TU Vision 2025+ is dealing with the impact of technical developments and innovations on different areas of society and is thus featuring the role of engineering science and technology as a pioneer and facilitator of processes for change.\n" +
            "As an introduction to the topic, Johann Kollegger, head of the research programme Reinforced Concrete and Solid Construction, will explain how engineering achievements can be transformed into sustainable innovations, driven by the aspiration to best meet society’s challenges. Innovations like these, eventually, enable us to showcase and measure the real value of engineering science.",
        "Marie Sklodowska-Curie Individual Fellowships (H2020-IF-2019) offer the possibility to gain individual research and training projects with researchers from abroad. These fellowships enable experienced researchers to go to a foreign host organisation for an individual research and training project and to co-design their own career development.",
        "When particles bond in free space, they normally create atoms or molecules. However, much more exotic bonding states can be produced inside solid objects.\n" +
            "\n" +
            "Researchers at TU Wien have now managed to utilise this: so-called “multi-particle exciton complexes” have been produced by applying electrical pulses to extremely thin layers of material made from tungsten and selenium or sulphur. These exciton clusters are bonding states made up of electrons and “holes” in the material and can be converted into light. The result is an innovative form of light-emitting diode in which the wavelength of the desired light can be controlled with high precision. These findings have now been published in the journal “Nature Communications”.",
        "The laser is the perfect light source: when being provided with energy, it generates light of a specific, well-defined colour. It is also possible, however, to create the exact opposite – an object that perfectly absorbs light of a particular colour and dissipates the energy almost completely.\n" +
            "\n" +
            "At TU Wien (Vienna), a method has now been developed to make use of this effect, even in very complicated systems in which light waves are randomly scattered in all directions. The method was developed by the team in Vienna with the help of computer simulations, and confirmed by experiments in cooperation with the University of Nice. This opens up new possibilities for all areas in science and engineering where wave phenomena play an important role. The new method has now been published in the journal \"Nature\".",
        "To bring together relevant engineering, analytical and biomedical expertise in Austria and bridge existing “research-to-product gaps” in microfluidics, lab-on-a-chip systems and organ-on-a-chip technology – that is what the Austrian Microfluidics Initiative (AMI), launched by TU Wien in 2017, is aiming at. The initiative is supported by BioNanoNet, its strategic positioning is implemented by Austrian universities and industrial partners. The spokesperson for the Austrian Microfluidics Initiative is Prof. Peter Ertl, also of TU Wien.",
        "Every day we lose valuable energy in the form of waste heat - in technical devices at home, but also in large energy systems. Part of it could be recovered with the help of the \"thermoelectric effect\". The heat flow from a hot device to the cold environment can be directly converted into electrical power. To achieve that, however, materials with very special properties are required. They have to be good electrical conductors, but bad thermal conductors – two requirements which are difficult to reconcile.",
        "Why does it always have to be so cold? We now know of a whole range of materials that – under certain conditions – conduct electrical current entirely without resistance. We call this phenomenon superconduction. All these materials do nonetheless experience a common problem: they only become superconducting at extremely low temperatures. The search to find theoretical computational methods to represent and understand this fact has been going on for many years. As yet, no one has fully succeeded in finding the solution. However, TU Wien has now developed a new method that enables a significantly better understanding of superconduction."
    };

    private final NewsRepository newsRepository;
    private final DBFileRepository dbFileRepository;
    private final Faker faker;

    public NewsDataGenerator(NewsRepository newsRepository, DBFileRepository dbFileRepository) {
        this.newsRepository = newsRepository;
        this.dbFileRepository = dbFileRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (newsRepository.count() > 0) {
            LOGGER.info("News already generated");
        } else {
            LOGGER.info("Generating {} news entries", DEMO_NEWS_TITLES.length);
            for (int i = 0; i < DEMO_NEWS_TITLES.length; i++) {
                Long fileId;
                try {
                    String fileName = (i+1) + ".jpg";
                    /*File dbFile = File.builder().fileName(fileName).fileType(".jpg").data(Files.readAllBytes(Paths.get("classpath:TEST_NEWS_IMAGES/" + fileName))).build();
                    fileId = dbFileRepository.save(dbFile).getId();*/
                    Path path = Paths.get(getClass().getClassLoader()
                        .getResource("TEST_NEWS_IMAGES/" + fileName).toURI());
                    byte[] fileBytes = Files.readAllBytes(path);
                    File dbFile = File.builder().fileName(fileName).fileType("jpg").data(fileBytes).build();
                    fileId = dbFileRepository.save(dbFile).getId();
                }
                catch (Exception e) {
                    LOGGER.info("Image for news entry with id {} could not be added: " + e.getMessage(), (i+1));
                    fileId = 0L;
                }
                News news = News.builder()
                    .title(DEMO_NEWS_TITLES[i])
                    .text(DEMO_NEWS_TEXTS[i])
                    .image(fileId.toString())
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
