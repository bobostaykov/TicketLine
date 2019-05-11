package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsSummaryMapper.class)
public interface NewsMapper {

    News detailedNewsDTOToNews(at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO detailedNewsDTO);

    at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO newsToDetailedNewsDTO(News one);

    List<at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    @Mapping(source = "text", target = "summary", qualifiedBy = NewsSummaryMapper.NewsSummary.class)
    at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO newsToSimpleNewsDTO(News one);

}