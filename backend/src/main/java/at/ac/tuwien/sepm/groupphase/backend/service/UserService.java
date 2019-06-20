package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.data.domain.Page;

public interface UserService {

    /**
     * Find all user entries.
     *
     * @param page number of the requested page
     * @param pageSize size of the requested Page
     * @return page of the found users
     */
    Page<UserDTO> findAll(Integer page, Integer pageSize) throws ServiceException;

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    UserDTO findOne(Long id) throws NotFoundException;

    UserDTO findUserByName(String name);

    /**
     * Create a user
     *
     * @param userDTO to add
     * @return created userDTO
     */
    UserDTO createUser(UserDTO userDTO) throws ServiceException;


    /**
     * Delete a user by id
     *
     * @param userId id of user to delete
     */
    void deleteUser(Long userId) throws ServiceException;

    /**
     * Find user by username.
     *
     * @param username name of user
     * @return found user
     */
    UserDTO findOneByUsername(String username);

    /**
     * @param userId id of the user that is to be unblocked
     * @return boolean if the operation was successful
     */
    boolean unblockUser(Long userId);

    /**
     * @param userId the id of the user that is to be blocked
     * @return boolean of the success of the operation
     */
    boolean blockUser(Long userId) throws ServiceException;


    /**
     * Get all users
     * @param page number of the requested page
     * @param pageSize size of the requested page
     * @return a page with users that are currently blocked
     */
    Page<UserDTO> getAllBlockedUsers(Integer page, Integer pageSize) throws ServiceException;
}
