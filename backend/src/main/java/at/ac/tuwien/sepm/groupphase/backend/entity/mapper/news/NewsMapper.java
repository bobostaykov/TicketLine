package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsSummaryMapper.class)
public interface NewsMapper {

    /**
     * Maps the DetailedNewsDTO object to a News object
     * @param detailedNewsDTO to map
     * @return the mapped News object
     */
    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    /**
     * Maps a News object to a DetailedNewsDTO object
     * @param one to map
     * @return the mapped DetailedNewsDTO object
     */
    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    /**
     * Maps a list of News objects to a list of SimpleNewsDTO objects
     * @param all to map
     * @return the mapped list of SimpleNewsDTO objects
     */
    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    /**
     * Maps a News object to a SimpleNewsDTO object
     * @param one to map
     * @return the mapped SimpleNewsDTO object
     */
    @Mapping(source = "text", target = "summary", qualifiedBy = NewsSummaryMapper.NewsSummary.class)
    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}