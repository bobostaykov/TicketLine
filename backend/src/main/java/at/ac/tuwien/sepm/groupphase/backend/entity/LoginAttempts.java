package at.ac.tuwien.sepm.groupphase.backend.entity;

import io.micrometer.core.lang.Nullable;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;

@Entity
@Table(name = "Login_attempts")
public class LoginAttempts {

    @Column

    @EmbeddedId
    private Long id;

    @OneToOne
    @MapsId(value = "id")
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "blocked")
     private boolean blocked;


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

        public LoginAttempts createLoginAttempts() {
            return new LoginAttempts(id, user, attempts, blocked);
        }
    }
}
