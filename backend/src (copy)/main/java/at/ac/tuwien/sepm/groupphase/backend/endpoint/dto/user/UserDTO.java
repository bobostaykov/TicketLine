package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "UserDTO", description = "A DTO for user entries via rest")
public class UserDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The unique name of the user")
    private String name;

    @ApiModelProperty(name = "The type of the user (admin/seller)")
    private UserType type;

    @ApiModelProperty(name = "The timestamp since when the user is in the system")
    private LocalDateTime userSince;

    @ApiModelProperty(name = "The timestamp of the last login of the user")
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

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
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

        UserDTO userDTO = (UserDTO) o;

        if (id != null ? !id.equals(userDTO.id) : userDTO.id != null) return false;
        if (userSince != null ? !userSince.equals(userDTO.userSince) : userDTO.userSince != null) return false;
        if (lastLogin != null ? !lastLogin.equals(userDTO.lastLogin) : userDTO.lastLogin != null) return false;
        if (name != null ? !name.equals(userDTO.name) : userDTO.name != null) return false;
        return type != null ? type.equals(userDTO.type) : userDTO.type == null;
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

    public static final class UserDTOBuilder {

        private Long id;
        private String name;
        private UserType type;
        private LocalDateTime userSince;
        private LocalDateTime lastLogin;

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserDTOBuilder type(UserType type) {
            this.type = type;
            return this;
        }

        public UserDTOBuilder userSince(LocalDateTime userSince) {
            this.userSince = userSince;
            return this;
        }

        public UserDTOBuilder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setName(name);
            userDTO.setType(type);
            userDTO.setUserSince(userSince);
            userDTO.setLastLogin(lastLogin);
            return userDTO;
        }
    }

}
