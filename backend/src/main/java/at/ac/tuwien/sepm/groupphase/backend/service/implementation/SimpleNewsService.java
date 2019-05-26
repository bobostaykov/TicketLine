package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.NewsEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;
    private final UserMapper userMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    public SimpleNewsService(NewsRepository newsRepository, UserRepository userRepository, NewsMapper newsMapper,
                             UserMapper userMapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<SimpleNewsDTO> findAll() {
        return newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc());
    }

    @Override
    public List<SimpleNewsDTO> findUnread(String username) {
        Optional<User> found = userRepository.findOneByUsername(username);
        if (!found.isEmpty()) {
            User user = found.get();
            List<News> readNews = user.getReadNews();
            List<News> allNews = newsRepository.findAll();
            return newsMapper.newsToSimpleNewsDTO(difference(allNews, readNews));
        } else {
            return newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc());
        }
    }


    private List<News> difference(List<News> all, List<News> drop) {
        List<News> result = new ArrayList<News>();
        for (News news : all) {
            if(!drop.contains(news)) {
                result.add(news);
            }
        }
        return result;
    }

    @Override
    public void addNewsFetch(String username, Long news_id) {
        Optional<User> found = userRepository.findOneByUsername(username);
        if (!found.isEmpty()) {
            User user = found.get();
            News news = newsRepository.getOne(news_id);
            List<News> newsRead = user.getReadNews();
            if (!newsRead.contains(news)) {
                newsRead.add(news);
                user.setReadNews(newsRead);
                userRepository.save(user);
            }
        }
    }

    @Override
    public DetailedNewsDTO findOne(Long id, String username) {
        this.addNewsFetch(username, id);
        return newsMapper.newsToDetailedNewsDTO(newsRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) {
        detailedNewsDTO.setPublishedAt(LocalDateTime.now());
        return newsMapper.newsToDetailedNewsDTO(newsRepository.save(newsMapper.detailedNewsDTOToNews(detailedNewsDTO)));
    }

}
