package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.userNews;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.userNews.UserNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNewsMapper {

    UserNewsDTO userNewsToUserNewsDTO(UserNews userNews);

    UserNews userNewsDTOToUserNews(UserNewsDTO userNewsDTO);

}
