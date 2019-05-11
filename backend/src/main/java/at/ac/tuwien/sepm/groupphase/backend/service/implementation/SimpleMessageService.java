package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserNews;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserNewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
    public List<Message> findUnread(String userName) {
        Optional<User> found = userRepository.findOneByName(userName);
        if (!found.isEmpty()) {
            Long userId = found.get().getId();
            return messageRepository.findUnread(userId);
        } else {
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

    @Override
    public Message publishMessageWithImage(Message message, MultipartFile image) {
        message.setPublishedAt(LocalDateTime.now());
        message.setImageName(image.getName());
        try {
            message.setImageData(image.getBytes());
        }
        catch (IOException e) {
            throw new ServiceException("Problem while processing attached image: " + e.getMessage(), e);
        }
        return messageRepository.save(message);
    }

}
