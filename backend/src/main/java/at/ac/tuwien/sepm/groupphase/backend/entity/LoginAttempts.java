package at.ac.tuwien.sepm.groupphase.backend.entity;

import io.micrometer.core.lang.Nullable;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;

@Entity
@Table(name = "Login_attempts")
public class LoginAttempts {

    @Column

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "blocked")
     private boolean blocked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
