package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "DetailedMessageDTO", description = "A simple DTO for message entries via rest")
public class SimpleMessageDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the message was published")
    private LocalDateTime publishedAt;

    @ApiModelProperty(required = true, readOnly = true, name = "The title of the message")
    private String title;

    @ApiModelProperty(required = true, readOnly = true, name = "The summary of the message")
    private String summary;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "SimpleMessageDTO{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", summary='" + summary + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMessageDTO that = (SimpleMessageDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (publishedAt != null ? !publishedAt.equals(that.publishedAt) : that.publishedAt != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return summary != null ? summary.equals(that.summary) : that.summary == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        return result;
    }

    public static MessageDTOBuilder builder() {
        return new MessageDTOBuilder();
    }

    public static final class MessageDTOBuilder {

        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;

        public MessageDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MessageDTOBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public MessageDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MessageDTOBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public SimpleMessageDTO build() {
            SimpleMessageDTO messageDTO = new SimpleMessageDTO();
            messageDTO.setId(id);
            messageDTO.setPublishedAt(publishedAt);
            messageDTO.setTitle(title);
            messageDTO.setSummary(summary);
            return messageDTO;
        }
    }
}