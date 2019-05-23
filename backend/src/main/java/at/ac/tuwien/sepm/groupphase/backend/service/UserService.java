package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;

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


    /**
     * Create a user
     *
     * @param userDTO to add
     * @return created userDTO
     */
    UserDTO createUser(UserDTO userDTO);

}
