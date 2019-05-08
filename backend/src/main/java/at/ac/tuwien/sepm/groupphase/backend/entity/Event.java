package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @Column(name="id")    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

    @Column(nullable = false, name = "name")
    @Size(max = 64)
    private String name;

    @Column(nullable = false, name = "eventType")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false, name = "duration")
    @Positive
    private Integer durationInMinutes;

    @Column(nullable = false, name = "description")
    @Size(max = 256)
    private String description;

    @Column(name = "content")
    @Size(max = 512)
    private String content;

    @ManyToMany
    @JoinTable(name = "participation",
        joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "artist_id", referencedColumnName = "id")})
    private List<Artist> participatingArtists;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "event")
    private List<Show> shows;

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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Artist> getParticipatingArtists() {
        return participatingArtists;
    }

    public void setParticipatingArtists(List<Artist> participatingArtists) {
        this.participatingArtists = participatingArtists;
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
        Event event = (Event) o;
        return id.equals(event.id) &&
            name.equals(event.name) &&
            eventType == event.eventType &&
            durationInMinutes.equals(event.durationInMinutes) &&
            description.equals(event.description) &&
            Objects.equals(content, event.content) &&
            participatingArtists.equals(event.participatingArtists) &&
            shows.equals(event.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, durationInMinutes, description, content, participatingArtists, shows);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventType=" + eventType +
            ", durationInMinutes=" + durationInMinutes +
            ", description='" + description + '\'' +
            ", content='" + content + '\'' +
            ", participatingArtists=" + participatingArtists +
            ", shows=" + shows +
            '}';
    }

    public static final class EventBuilder {
        private Long id;
        private String name;
        private EventType eventType;
        private Integer durationInMinutes;
        private String description;
        private String content;
        private List<Artist> participatingArtists;
        private List<Show> shows;

        private EventBuilder() {}

        public EventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventBuilder durationInMinutes(Integer durationInMinutes) {
            this.durationInMinutes = durationInMinutes;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EventBuilder participatingArtists(List<Artist> participatingArtists) {
            this.participatingArtists = participatingArtists;
            return this;
        }

        public EventBuilder shows(List<Show> shows) {
            this.shows = shows;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setName(name);
            event.setEventType(eventType);
            event.setDurationInMinutes(durationInMinutes);
            event.setDescription(description);
            event.setContent(content);
            event.setParticipatingArtists(participatingArtists);
            event.setShows(shows);
            return event;
        }
    }
}
