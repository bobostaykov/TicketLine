package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.userNews.UserNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.userNews.UserNewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserNewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserNewsService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class UserNewsServiceImpl implements UserNewsService {

    private final UserNewsRepository userNewsRepository;
    private final UserNewsMapper userNewsMapper;

    public UserNewsServiceImpl(UserNewsRepository userNewsRepository, UserNewsMapper userNewsMapper) {
        this.userNewsRepository = userNewsRepository;
        this.userNewsMapper = userNewsMapper;
    }

    @Override
    public void addNewsFetch(UserNewsDTO userNewsDTO) {
        try {
            if (userNewsRepository.findOneByUserIdAndNewsId(userNewsDTO.getUserId(), userNewsDTO.getNewsId()).isEmpty()) {
                userNewsRepository.save(userNewsMapper.userNewsDTOToUserNews(userNewsDTO));
            }
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
