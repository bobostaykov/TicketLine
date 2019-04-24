package at.ac.tuwien.sepm.groupphase.backend.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class UserLatestNewsFetch {

    @Id
    @ApiModelProperty(readOnly = true, name = "userName")
    private String userName;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the news where fetched latest")
    private LocalDateTime lastFetchTimestamp;

    public String getUserName() {
        return userName;
    }

    public void setUserName(Long id) {
        this.userName = userName;
    }

    public LocalDateTime getLastFetchTimestamp() {
        return lastFetchTimestamp;
    }

    public void setLastFetchTimestamp(LocalDateTime publishedAt) {
        this.lastFetchTimestamp = lastFetchTimestamp;
    }


    /*public static MessageBuilder builder() {
        return new MessageBuilder();
    }*/

    @Override
    public String toString() {
        return "UserLatestNewsFetch{" +
            "userName=" + userName +
            ", lastFetchTimestamp=" + lastFetchTimestamp +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLatestNewsFetch that = (UserLatestNewsFetch) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return lastFetchTimestamp != null ? !lastFetchTimestamp.equals(that.lastFetchTimestamp) : that.lastFetchTimestamp != null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (lastFetchTimestamp != null ? lastFetchTimestamp.hashCode() : 0);
        return result;
    }

    /*
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

        public UserLatestNewsFetch build() {
            UserLatestNewsFetch message = new UserLatestNewsFetch();
            message.setId(id);
            message.setPublishedAt(publishedAt);
            message.setTitle(title);
            message.setText(text);
            return message;
        }
    }
    */
}