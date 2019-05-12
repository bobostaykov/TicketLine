package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "name")
    @Size(max = 64)
    private String name;

    @Column(nullable = false, name = "eventType")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false, name = "description")
    @Size(max = 256)
    private String description;

    @Column(name = "content")
    @Size(max = 512)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private Artist artist;

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

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id) &&
            name.equals(event.name) &&
            eventType == event.eventType &&
            Objects.equals(description, event.description) &&
            Objects.equals(content, event.content) &&
            Objects.equals(artist, event.artist) &&
            Objects.equals(shows, event.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, description, content, artist, shows);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventType=" + eventType +
            ", description='" + description + '\'' +
            ", content='" + content + '\'' +
            ", artist=" + artist +
            ", shows=" + shows +
            '}';
    }

    public static final class EventBuilder {
        private Long id;
        private String name;
        private EventType eventType;
        private String description;
        private String content;
        private Artist artist;
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

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EventBuilder artist(Artist artist) {
            this.artist = artist;
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
            event.setDescription(description);
            event.setContent(content);
            event.setArtist(artist);
            event.setShows(shows);
            return event;
        }
    }
}
