package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

/**
 * A common Data Generator Interface for all generators
 */
public interface DataGenerator {
    /**
     * Generates objects from that instance and saves them in DB
     */
    void generate();

    /**
     * Calculates custom modulo operation (for number starting at 1)
     *
     * @param num number to calulate special modulo from
     * @param modulo number to calculate special modulo with
     * @return special modulo result
     */
    default Long customMod(Long num, int modulo) {
        return ((num-1) % modulo) + 1;
    }

    /**
     * Calculates custom modulo operation (for number starting at 1) - integer version
     *
     */
    default Integer customModInt(Long num, int modulo) {
        return Math.toIntExact(customMod(num, modulo));
    }
}
