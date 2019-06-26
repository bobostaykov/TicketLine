package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.annotation.SeatsXorSectorsConstraint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "HallDTO", description = "A DTO to transfer hall entities between backend and frontend")
@SeatsXorSectorsConstraint(message = "Exactly one of either seats or sectors must be set")
public class HallDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The name of the hall", required = true)
    @NotBlank(message = "Hall name was not set")
    private String name;

    @ApiModelProperty(name = "The location of the hall", required = true)
    @NotNull(message = "Hall Location was not set")
    private LocationDTO location;

    @ApiModelProperty(name = "List of seats in the hall")
    @Valid
    private List<SeatDTO> seats;

    @ApiModelProperty(name = "List of sectors in the hall")
    @Valid
    private List<SectorDTO> sectors;

    @ApiModelProperty(name = "Boolean that declares whether editing is currently enabled for the hall",
        notes = "Depends on whether one or multiple shows have already been set for the hall")
    private Boolean editingEnabled;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<SectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<SectorDTO> sectors) {
        this.sectors = sectors;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public static HallDTOBuilder builder() {
        return new HallDTOBuilder();
    }

    public Boolean getEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(Boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallDTO hallDTO = (HallDTO) o;
        return Objects.equals(id, hallDTO.getId()) &&
            Objects.equals(name, hallDTO.getName()) &&
            Objects.equals(location, hallDTO.getLocation()) &&
            Objects.equals(seats, hallDTO.getSeats()) &&
            Objects.equals(sectors, hallDTO.getSectors()) &&
            Objects.equals(editingEnabled, hallDTO.getEditingEnabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, seats, sectors, editingEnabled);
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", location=" + location +
            ", seats=" + seats +
            ", sectors=" + sectors +
            ", editingEnabled=" + editingEnabled +
            '}';
    }

    public static final class HallDTOBuilder {

        private Long id;
        private String name;
        private LocationDTO location;
        private List<SeatDTO> seats;
        private List<SectorDTO> sectors;
        private Boolean editingEnabled;

        private HallDTOBuilder() {
        }

        public HallDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HallDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HallDTOBuilder location(LocationDTO location) {
            this.location = location;
            return this;
        }

        public HallDTOBuilder seats(List<SeatDTO> seats) {
            this.seats = seats;
            return this;
        }

        public HallDTOBuilder sectors(List<SectorDTO> sectors) {
            this.sectors = sectors;
            return this;
        }

        public HallDTOBuilder editingEnabled(Boolean editingEnabled){
            this.editingEnabled = editingEnabled;
            return this;
        }

        public HallDTO build() {
            HallDTO hall = new HallDTO();
            hall.setId(id);
            hall.setName(name);
            hall.setLocation(location);
            hall.setSeats(seats);
            hall.setSectors(sectors);
            hall.setEditingEnabled(editingEnabled);
            return hall;
        }
    }
}
