package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps User object to UserDTO object
     * @param user to map
     * @return the mapped UserDTO object
     */
    UserDTO userToUserDTO(User user);

    /**
     * Maps UserDTO object to User object
     * @param userDTO to map
     * @return the mapped User object
     */
    User userDTOToUser(UserDTO userDTO);

}
