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
import java.util.ArrayList;
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
        User user = userRepository.findOneByName(username).get();
        News news = newsRepository.getOne(news_id);
        List<News> newsRead = user.getReadNews();
        newsRead.add(news);
        user.setReadNews(newsRead);
        userRepository.save(user);
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
