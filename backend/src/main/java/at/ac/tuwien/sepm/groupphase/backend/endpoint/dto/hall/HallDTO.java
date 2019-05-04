package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;

import java.util.List;
import java.util.Objects;

public class HallDTO {

    private Long id;

    private String name;

    private List<Show> shows;

    public HallDTO() {
    }

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

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallDTO hallDTO = (HallDTO) o;
        return id.equals(hallDTO.id) &&
            name.equals(hallDTO.name) &&
            shows.equals(hallDTO.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shows);
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", shows=" + shows +
            '}';
    }
}
