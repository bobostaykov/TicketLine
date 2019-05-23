package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    List<UserDTO> userToUserDTO(List<User> all);

    User userDTOToUser(UserDTO userDTO);

}
