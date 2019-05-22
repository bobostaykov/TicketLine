package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;

    public SimpleNewsService(NewsRepository newsRepository, UserRepository userRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    public List<SimpleNewsDTO> findAll() {
        return newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc());
    }

    @Override
    public List<SimpleNewsDTO> findUnread(String userName) {
        Optional<User> found = userRepository.findOneByUsername(userName);
        if (!found.isEmpty()) {
            Long userId = found.get().getId();
            return newsMapper.newsToSimpleNewsDTO(newsRepository.findUnread(userId));
        } else {
            return newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc());
        }
    }

    @Override
    public DetailedNewsDTO findOne(Long id) {
        return newsMapper.newsToDetailedNewsDTO(newsRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) {
        detailedNewsDTO.setPublishedAt(LocalDateTime.now());
        return newsMapper.newsToDetailedNewsDTO(newsRepository.save(newsMapper.detailedNewsDTOToNews(detailedNewsDTO)));
    }

}
