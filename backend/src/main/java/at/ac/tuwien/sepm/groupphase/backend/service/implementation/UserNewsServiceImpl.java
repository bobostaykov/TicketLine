package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserNews;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserNewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserNewsService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class UserNewsServiceImpl implements UserNewsService {

    private final UserNewsRepository userNewsRepository;

    public UserNewsServiceImpl(UserNewsRepository userNewsRepository) {
        this.userNewsRepository = userNewsRepository;
    }

    @Override
    public void addNewsFetch(UserNews userNews) {
        try {
            if (userNewsRepository.findOneByUserIdAndNewsId(userNews.getUserId(), userNews.getNewsId()).isEmpty()) {
                userNewsRepository.save(userNews);
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
