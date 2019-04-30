package at.ac.tuwien.sepm.groupphase.backend.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_message_id")
    @SequenceGenerator(name = "seq_message_id", sequenceName = "seq_message_id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "userName")
    private String userName;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the news where fetched latest")
    private LocalDateTime lastFetchTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getLastFetchTimestamp() {
        return lastFetchTimestamp;
    }

    public void setLastFetchTimestamp(LocalDateTime lastFetchTimestamp) {
        this.lastFetchTimestamp = lastFetchTimestamp;
    }


    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            "userName=" + userName +
            ", lastFetchTimestamp=" + lastFetchTimestamp +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return lastFetchTimestamp != null ? !lastFetchTimestamp.equals(that.lastFetchTimestamp) : that.lastFetchTimestamp != null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (lastFetchTimestamp != null ? lastFetchTimestamp.hashCode() : 0);
        return result;
    }


    public static final class UserBuilder {
        private Long id;
        private String userName;
        private LocalDateTime lastFetchTimestamp;

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder lastFetchTimestamp(LocalDateTime lastFetchTimestamp) {
            this.userName = userName;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUserName(userName);
            user.setLastFetchTimestamp(lastFetchTimestamp);
            return user;
        }
    }

}