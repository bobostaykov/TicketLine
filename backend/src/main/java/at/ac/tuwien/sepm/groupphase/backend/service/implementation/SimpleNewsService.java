package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
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
    public List<SimpleNewsDTO> findUnread(String userName) {
        Optional<User> found = userRepository.findOneByUsername(userName);
        if (!found.isEmpty()) {
            UserDTO userDTO = userMapper.userToUserDTO(found.get());
            List<SimpleNewsDTO> readNews = userDTO.getReadNews();
            List<SimpleNewsDTO> allNews = newsMapper.newsToSimpleNewsDTO(newsRepository.findAll());
            return difference(allNews, readNews);
        } else {
            return newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc());
        }
    }


    private List<SimpleNewsDTO> difference(List<SimpleNewsDTO> all, List<SimpleNewsDTO> drop) {
        List<SimpleNewsDTO> result = new ArrayList<SimpleNewsDTO>();
        for (SimpleNewsDTO news : all) {
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
            UserDTO user = userMapper.userToUserDTO(found.get());
            SimpleNewsDTO news = newsMapper.newsToSimpleNewsDTO(newsRepository.getOne(news_id));
            List<SimpleNewsDTO> newsRead = user.getReadNews();
            newsRead.add(news);
            user.setReadNews(newsRead);
            userRepository.save(userMapper.userDTOToUser(user));
        }
    }

    @Override
    public DetailedNewsDTO findOne(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.addNewsFetch(username, id);
        return newsMapper.newsToDetailedNewsDTO(newsRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO detailedNewsDTO) {
        detailedNewsDTO.setPublishedAt(LocalDateTime.now());
        return newsMapper.newsToDetailedNewsDTO(newsRepository.save(newsMapper.detailedNewsDTOToNews(detailedNewsDTO)));
    }

}
