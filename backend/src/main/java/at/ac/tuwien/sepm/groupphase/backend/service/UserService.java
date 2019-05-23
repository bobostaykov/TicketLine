package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;

import java.util.List;
import java.util.Optional;

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

}
