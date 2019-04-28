package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, name = "user_since")
    private LocalDateTime userSince;

    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getUserSince() {
        return userSince;
    }

    public void setUserSince(LocalDateTime userSince) {
        this.userSince = userSince;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", userSince=" + userSince +
            ", lastLogin=" + lastLogin +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (userSince != null ? !userSince.equals(user.userSince) : user.userSince != null) return false;
        if (lastLogin != null ? !lastLogin.equals(user.lastLogin) : user.lastLogin != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return type != null ? type.equals(user.type) : user.type == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (userSince != null ? userSince.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    public static final class UserBuilder {

        private Long id;
        private String name;
        private String type;
        private LocalDateTime userSince;
        private LocalDateTime lastLogin;

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder type(String type) {
            this.type = type;
            return this;
        }

        public UserBuilder userSince(LocalDateTime userSince) {
            this.userSince = userSince;
            return this;
        }

        public UserBuilder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setType(type);
            user.setUserSince(userSince);
            user.setLastLogin(lastLogin);
            return user;
        }
    }

}