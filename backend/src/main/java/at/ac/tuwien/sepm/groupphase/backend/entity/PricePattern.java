package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;
import java.util.Map;

@Entity
public class PricePattern {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    private Map<PriceCategory, Double> priceMapping;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<PriceCategory, Double> getPriceMapping() {
        return priceMapping;
    }

    public void setPriceMapping(Map<PriceCategory, Double> priceMapping) {
        this.priceMapping = priceMapping;
    }

    public static PricePatternBuilder builder(){
        return new PricePatternBuilder();
    }

    public static class PricePatternBuilder {
        private String name;
        private Map<PriceCategory, Double> priceMapping;

        private PricePatternBuilder(){}

        public PricePatternBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PricePatternBuilder setPriceMapping(Map<PriceCategory, Double> priceMapping) {
            this.priceMapping = priceMapping;
            return this;
        }

        public PricePattern createPricePattern() {
            PricePattern pricePattern = new PricePattern();
            pricePattern.setPriceMapping(priceMapping);
            pricePattern.setName(name);
            return pricePattern;
        }
    }
}
