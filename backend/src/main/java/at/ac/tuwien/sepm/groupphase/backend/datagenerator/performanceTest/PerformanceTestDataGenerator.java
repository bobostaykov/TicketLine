package at.ac.tuwien.sepm.groupphase.backend.datagenerator.performanceTest;

/**
 * A common Data Generator Interface for all generators
 */
public abstract class PerformanceTestDataGenerator {
    /**
     * Generates objects from that instance and saves them in DB
     */
    abstract void generate();

    /**
     * Calculates custom modulo operation (for number starting at 1)
     *
     * @param num number to calulate special modulo from
     * @param modulo number to calculate special modulo with
     * @return special modulo result
     */
    Long customMod(Long num, int modulo) {
        return ((num-1) % modulo) + 1;
    }

    /**
     * Calculates custom modulo operation (for number starting at 1) - integer version
     *
     */
    Integer customModInt(Long num, int modulo) {
        return Math.toIntExact(customMod(num, modulo));
    }

    final static int NUM_OF_ARTISTS = 25;
    final static int NUM_OF_CUSTOMERS = 1000;
    final static int NUM_OF_TICKETS = 1000; // MAX: NUM_OF_SHOWS * 10
    final static int NUM_OF_EVENTS = 200;
    final static int NUM_OF_SHOWS = 200;
    final static int NUM_OF_LOCATIONS = 25;
    final static int NUM_OF_HALLS = 25;
    final static int NUM_OF_USERS = 25;
    final static int NUM_OF_NEWS = 1000;
    final static int NUM_OF_SEATS_PER_HALL = 100;
    final static int NUM_OF_SEAT_ROWS_PER_HALL = 10;
    final static int NUM_OF_SECTORS_PER_HALL = 10;

    final static int MAX_EVENT_DURATION_IN_MINUTES = 120;
    final static int MIN_EVENT_DURATION_IN_MINUTES = 30;
    final static Double TICKET_PRICE = 10.0;
}
