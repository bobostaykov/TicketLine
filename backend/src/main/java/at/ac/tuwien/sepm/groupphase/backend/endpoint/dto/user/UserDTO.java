package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "UserDTO", description = "A DTO for user entries via rest")
public class UserDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The unique username of the user")
    @NotBlank(message = "username may not be blank")
    private String username;

    @ApiModelProperty(name = "The password of the user")
    private String password;

    @ApiModelProperty(name = "The type of the user (admin/seller)")
    private UserType type;

    @ApiModelProperty(name = "The timestamp since when the user is in the system")
    private LocalDateTime userSince;

    @ApiModelProperty(name = "The timestamp of the last login of the user")
    private LocalDateTime lastLogin;

    @ApiModelProperty(name = "List of read news by user")
    private List<SimpleNewsDTO> readNews;

    public UserDTO() {
        this.readNews = new ArrayList<SimpleNewsDTO>();
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

    public void setReadNews(List<SimpleNewsDTO> readNews) {
        this.readNews = readNews;
    }

    public List<SimpleNewsDTO> getReadNews() {
        return readNews;
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
            ", username='" + username + '\'' +
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
        if (username != null ? !username.equals(userDTO.username) : userDTO.username != null) return false;
        if (password != null ? !password.equals(userDTO.password) : userDTO.password != null) return false;
        if (readNews != null ? !readNews.equals(userDTO.readNews) : userDTO.readNews != null) return false;
        return type != null ? type.equals(userDTO.type) : userDTO.type == null;
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

    public static final class UserDTOBuilder {

        private Long id;
        private String username;
        private String password;
        private UserType type;
        private LocalDateTime userSince;
        private LocalDateTime lastLogin;
        private List<SimpleNewsDTO> readNews;

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder password(String password) {
            this.password = password;
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

        public UserDTOBuilder readNews(List<SimpleNewsDTO> readNews) {
            this.readNews = readNews;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setType(type);
            userDTO.setUserSince(userSince);
            userDTO.setLastLogin(lastLogin);
            userDTO.setReadNews(readNews);
            return userDTO;
        }
    }

}
