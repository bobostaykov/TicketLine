package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;

import javax.persistence.*;
import java.util.Map;

@Entity
public class PricePattern {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "priceMapping")
    private Map<PriceCategory, Double> priceMapping;
}
