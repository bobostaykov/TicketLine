package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsMapper newsMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

    public NewsServiceImpl(NewsRepository newsRepository, UserRepository userRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsMapper = newsMapper;
    }

    @Override
    public Page<SimpleNewsDTO> findAll(Integer page, Integer pageSize) {
        LOGGER.info("News Service: Get all news entries (short version)");
        if(pageSize == null){
            pageSize = 12;
        }
        if(page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return newsRepository.findAllByOrderByPublishedAtDesc(pageable).map(newsMapper::newsToSimpleNewsDTO);
    }

    @Override
    public Page<SimpleNewsDTO> findUnread(String username, Integer page, Integer pageSize) {
        LOGGER.info("News Service: Get all unread news entries (short version) for user with username " + username);
        if(pageSize == null){
            pageSize = 12;
        }
        if(page < 0) {
            throw new IllegalArgumentException("Not a valid page.");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Pageable allPages = Pageable.unpaged();
        Optional<User> found = userRepository.findOneByUsername(username);
        if (!found.isEmpty()) {
            User user = found.get();
            List<News> readNews = user.getReadNews();
            List<News> allNews = newsRepository.findAllByOrderByPublishedAtDesc(allPages).getContent();
            List<SimpleNewsDTO> resultedList = newsMapper.newsToSimpleNewsDTO(difference(allNews, readNews));
            int start = (int)pageable.getOffset();
            int end = (start + pageable.getPageSize()) > resultedList.size() ? resultedList.size() : (start + pageable.getPageSize());
            return new PageImpl<>(resultedList.subList(start, end), pageable, resultedList.size());
        } else {
            return newsRepository.findAllByOrderByPublishedAtDesc(pageable).map(newsMapper::newsToSimpleNewsDTO);
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
        LOGGER.info("News Service: Mark news entry with id " + news_id + " as read by user with username " + username);
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
        LOGGER.info("News Service: Get long/ detailed version of news entry with id " + id);
        this.addNewsFetch(username, id);
        return newsMapper.newsToDetailedNewsDTO(newsRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) {
        LOGGER.info("News Service: Publish a new news entry: " + detailedNewsDTO.toString());
        detailedNewsDTO.setPublishedAt(LocalDateTime.now());
        return newsMapper.newsToDetailedNewsDTO(newsRepository.save(newsMapper.detailedNewsDTOToNews(detailedNewsDTO)));
    }

}
