package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;

//the class is unfinished, add missing attributes
@Entity
public class Hall {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(length = 64)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hall")
    private List<Show> shows;
}
