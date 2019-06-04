package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;

public interface UserService {

    /**
     * Find all user entries.
     *
     * @return all users
     */
    List<UserDTO> findAll();

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    UserDTO findOne(Long id);

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
    void deleteUser(Long userId);

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
     * @param userId
     * @return
     */
    boolean blockUser(Long userId) throws ServiceException;


    /**
     * @return
     */
    List<UserDTO> getAllBlockedUsers();
}
