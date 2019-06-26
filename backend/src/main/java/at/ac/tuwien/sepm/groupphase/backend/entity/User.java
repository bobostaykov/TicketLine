package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    private String username;

    @Column(nullable = false)
    @Size(max = 255)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(nullable = false, name = "user_since")
    private LocalDateTime userSince;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @OneToMany(fetch = FetchType.EAGER)
    private List<News> readNews;

    @OneToOne(mappedBy =  "user",cascade = { CascadeType.ALL}, orphanRemoval = true, optional = false)
    private LoginAttempts loginAttempts;


    public User() {
        this.readNews = new ArrayList<News>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<News> getReadNews() {
        return readNews;
    }

    public void setReadNews(List<News> readNews) {
        this.readNews = readNews;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
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

    public LoginAttempts getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(LoginAttempts loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", type=" + type +
            ", userSince=" + userSince +
            ", lastLogin=" + lastLogin +
            ", readNews="+ readNews != null ? readNews.toString() : "" + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (userSince != null ? !userSince.equals(user.userSince) : user.userSince != null) return false;
        if (lastLogin != null ? !lastLogin.equals(user.lastLogin) : user.lastLogin != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (readNews != null ? !readNews.equals(user.readNews) : user.readNews != null) return false;
        return type != null ? type.equals(user.type) : user.type == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (userSince != null ? userSince.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (readNews != null ? readNews.hashCode() : 0);
        return result;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    public static final class UserBuilder {

        private Long id;
        private String username;
        private String password;
        private UserType type;
        private LocalDateTime userSince;
        private LocalDateTime lastLogin;
        private List<News> readNews;
        private LoginAttempts loginAttempts;

        private UserBuilder() {
            this.readNews = new ArrayList<News>();
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder type(UserType type) {
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

        public UserBuilder readNews(List<News> readNews) {
            this.readNews = readNews;
            return this;
        }
        public UserBuilder loginAttempts(LoginAttempts loginAttempts){
            this.loginAttempts = loginAttempts;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setType(type);
            user.setUserSince(userSince);
            user.setLastLogin(lastLogin);
            user.setReadNews(readNews);
            user.setLoginAttempts(loginAttempts);
            return user;
        }
    }

}