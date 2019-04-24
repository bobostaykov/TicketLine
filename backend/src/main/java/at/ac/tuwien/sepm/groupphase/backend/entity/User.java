package at.ac.tuwien.sepm.groupphase.backend.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @ApiModelProperty(readOnly = true, name = "userName")
    private String userName;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the news where fetched latest")
    private LocalDateTime lastFetchTimestamp;

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
            "userName=" + userName +
            ", lastFetchTimestamp=" + lastFetchTimestamp +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return lastFetchTimestamp != null ? !lastFetchTimestamp.equals(that.lastFetchTimestamp) : that.lastFetchTimestamp != null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (lastFetchTimestamp != null ? lastFetchTimestamp.hashCode() : 0);
        return result;
    }


    public static final class UserBuilder {
        private String userName;
        private LocalDateTime lastFetchTimestamp;

        private UserBuilder() {
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
            user.setUserName(userName);
            user.setLastFetchTimestamp(lastFetchTimestamp);
            return user;
        }
    }

}