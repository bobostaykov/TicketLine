package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_message_id")
    @SequenceGenerator(name = "seq_message_id", sequenceName = "seq_message_id")
    private Long id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false, length = 10_000)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (publishedAt != null ? !publishedAt.equals(message.publishedAt) : message.publishedAt != null) return false;
        if (title != null ? !title.equals(message.title) : message.title != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    public static final class MessageBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String text;

        private MessageBuilder() {
        }

        public MessageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MessageBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public MessageBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MessageBuilder text(String text) {
            this.text = text;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setId(id);
            message.setPublishedAt(publishedAt);
            message.setTitle(title);
            message.setText(text);
            return message;
        }
    }
}