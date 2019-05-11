package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of al message entries
     */
    List<Message> findAll();

    /**
     * Find latest message entries ordered by published at date (descending).
     *
     * @param username the username to get the latest news for
     * @return ordered list of latest message entries
     */
    List<Message> findUnread(String username);

    /**
     * Find a single message entry by id.
     *
     * @param id the id of the message entry
     * @return the message entry
     */
    Message findOne(Long id);


    /**
     * Publish a single message entry
     *
     * @param message to publish
     * @return published message entry
     */
    Message publishMessage(Message message);

    /**
     * Publish a single message entry
     *
     * @param message to publish
     * @param image to be attached to the message entry
     * @return published message entry
     */
    Message publishMessageWithImage(Message message, MultipartFile image);

}
