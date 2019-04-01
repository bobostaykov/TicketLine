package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public SimpleMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAllByOrderByPublishedAtDesc();
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
