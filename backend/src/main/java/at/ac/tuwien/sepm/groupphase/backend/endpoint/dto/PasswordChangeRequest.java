package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.authentication.AuthenticationRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "passwordChangeDTO", description = "DTO to pass new user credentials to the backend")
public class PasswordChangeRequest {
    @ApiModelProperty(required = true, name = "The unique username of the user", example = "admin")
    private String username;

    @ApiModelProperty(required = true, name = "The password of the user", example = "password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordChangeRequest that = (PasswordChangeRequest) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public static PasswordChangeRequest.PasswordChangeRequestBuilder builder() {
        return new PasswordChangeRequestBuilder();
    }

    public static final class PasswordChangeRequestBuilder {

        private String username;
        private String password;

        public PasswordChangeRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PasswordChangeRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PasswordChangeRequest build() {
            PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
            passwordChangeRequest.setUsername(username);
            passwordChangeRequest.setPassword(password);
            return passwordChangeRequest;
        }
    }
}