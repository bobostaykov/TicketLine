package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.userNews;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "UserNewsDTO", description = "A DTO for userNews entries via rest")
public class UserNewsDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The key id of the user")
    private Long userId;

    @ApiModelProperty(name = "The key id of the news")
    private Long newsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public static UserNewsDTOBuilder builder() {
        return new UserNewsDTOBuilder();
    }

    @Override
    public String toString() {
        return "UserNewsDTO{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", newsId=" + newsId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNewsDTO userNewsDTO = (UserNewsDTO) o;

        if (id != null ? !id.equals(userNewsDTO.id) : userNewsDTO.id != null) return false;
        if (userId != null ? !userId.equals(userNewsDTO.userId) : userNewsDTO.userId != null) return false;
        return newsId != null ? newsId.equals(userNewsDTO.newsId) : userNewsDTO.newsId== null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (newsId != null ? newsId.hashCode() : 0);
        return result;
    }

    public static final class UserNewsDTOBuilder {

        private Long id;
        private Long userId;
        private Long newsId;

        public UserNewsDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserNewsDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserNewsDTOBuilder newsId(Long newsId) {
            this.newsId = newsId;
            return this;
        }


        public UserNewsDTO build() {
            UserNewsDTO userNewsDTO = new UserNewsDTO();
            userNewsDTO.setId(id);
            userNewsDTO.setUserId(userId);
            userNewsDTO.setNewsId(newsId);
            return userNewsDTO;
        }
    }

}
