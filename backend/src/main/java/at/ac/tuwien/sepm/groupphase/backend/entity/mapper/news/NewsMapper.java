package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsSummaryMapper.class)
public interface NewsMapper {

    @Mapping(source = "image", target = "imageId")
    News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    @Mapping(source = "imageId", target = "image")
    DetailedNewsDTO newsToDetailedNewsDTO(News one);

    List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    @Mapping(source = "text", target = "summary", qualifiedBy = NewsSummaryMapper.NewsSummary.class)
    SimpleNewsDTO newsToSimpleNewsDTO(News one);

}