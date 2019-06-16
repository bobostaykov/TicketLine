package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "passwordChangeDTO", description = "DTO to pass new user credentials to the backend")
public class PasswordChangeRequest {
    @ApiModelProperty(required = true, name = "The password of the user", example = "password")
    private String password;

    @ApiModelProperty(required = true, name = "the id of the user", example = "1")
    public Long id;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static PasswordChangeRequest.PasswordChangeRequestBuilder builder() {
        return new PasswordChangeRequestBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordChangeRequest that = (PasswordChangeRequest) o;
        return Objects.equals(password, that.password) &&
            Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, id);
    }

    @Override
    public String toString() {
        return "PasswordChangeRequest{" +
            "password='" + password + '\'' +
            ", id=" + id +
            '}';
    }

    public static final class PasswordChangeRequestBuilder {

        private Long userId;
        private String password;

        public PasswordChangeRequestBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public PasswordChangeRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PasswordChangeRequest build() {
            PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
            passwordChangeRequest.setId(userId);
            passwordChangeRequest.setPassword(password);
            return passwordChangeRequest;
        }
    }
}