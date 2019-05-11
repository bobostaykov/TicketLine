package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

/**
 * A common interface for all data generators
 */
public interface DataGenerator {

    /**
     * Generates data and saves it in the database.
     */
    void generate();
}
