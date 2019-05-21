package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

/**
 * A common Data Generator Interface for all generators
 */
public interface DataGenerator {
    /**
     * Generates objects from that instance and saves them in DB
     */
    void generate();
}
