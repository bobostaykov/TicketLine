package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Login_attempts")
public class LoginAttempts {

    public LoginAttempts() { }

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "blocked")
     private boolean blocked;

    public void setUserSynch(User user) {
        if (user == null) {
            if (this.user != null) {
                this.user.setLoginAttempts(null);
            }
        }
        else {
            user.setLoginAttempts(this);
        }
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginAttempts attempts1 = (LoginAttempts) o;
        return attempts == attempts1.attempts &&
            blocked == attempts1.blocked &&
            Objects.equals(id, attempts1.id) &&
            Objects.equals(user, attempts1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, attempts, blocked);
    }

    @Override
    public String toString() {
        return "LoginAttempts{" +
            "id=" + id +
            ", user=" + user +
            ", attempts=" + attempts +
            ", blocked=" + blocked +
            '}';
    }

    public LoginAttempts(Long id, User user, int attempts, boolean blocked) {
        this.id = id;
        this.user = user;
        this.attempts = attempts;
        this.blocked = blocked;
    }

    public Long getId() {
        return id;
    }

    public void setId() {this.id = id;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumberOfAttempts() {
        return attempts;
    }

    public void setNumberOfAttempts(int numberOfAttempts) {
        this.attempts = numberOfAttempts;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public static LoginAttemptsBuilder builder(){return new LoginAttemptsBuilder();}

    public static class LoginAttemptsBuilder {
        private Long id;
        private User user;
        private int attempts;
        private boolean blocked;

        private LoginAttemptsBuilder(){}

        public LoginAttemptsBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public LoginAttemptsBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public LoginAttemptsBuilder setAttempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public LoginAttemptsBuilder setBlocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public LoginAttempts build() {
            return new LoginAttempts(id, user, attempts, blocked);
        }
    }
}
