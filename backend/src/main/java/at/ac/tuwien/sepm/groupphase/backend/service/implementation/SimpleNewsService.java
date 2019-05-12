package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
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

    public SimpleNewsService(NewsRepository newsRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public List<News> findUnread(String userName) {
        Optional<User> found = userRepository.findOneByName(userName);
        if (!found.isEmpty()) {
            Long userId = found.get().getId();
            return newsRepository.findUnread(userId);
        } else {
            return newsRepository.findAllByOrderByPublishedAtDesc();
        }
    }

    @Override
    public News findOne(Long id) {
        News returnValue = newsRepository.findOneById(id).orElseThrow(NotFoundException::new);
        return returnValue;
    }

    @Override
    public News publishNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

}
