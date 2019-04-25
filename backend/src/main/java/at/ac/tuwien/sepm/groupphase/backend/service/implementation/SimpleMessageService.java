package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public SimpleMessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public List<Message> findLatest(String userName) {
        Optional<User> lastFetch = userRepository.findOneByUserName(userName);
        if (lastFetch.isPresent()) {
            return messageRepository.findAllAfter(lastFetch.get().getLastFetchTimestamp());
        }
        else {
            User user = new User();
            user.setUserName(userName);
            user.setLastFetchTimestamp(LocalDateTime.now());
            userRepository.save(user);
            return messageRepository.findAllByOrderByPublishedAtDesc();
        }
    }

    @Override
    public Message findOne(Long id) {
        return messageRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Message publishMessage(Message message) {
        message.setPublishedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

}
