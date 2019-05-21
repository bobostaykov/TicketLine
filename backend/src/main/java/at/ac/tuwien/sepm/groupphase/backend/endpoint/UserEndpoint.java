package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Api(value = "users")
public class UserEndpoint {

    private final LoginAttemptService loginAttemptService;
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.loginAttemptService = loginAttemptService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all users", authorizations = {@Authorization(value = "apiKey")})
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get a user by id", authorizations = {@Authorization(value = "apiKey")})
    public UserDTO find(@PathVariable Long id) {
        return userService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a user", authorizations = {@Authorization(value = "apiKey")})
    public UserDTO create(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
        User user = userService.createUser(userMapper.userDTOToUser(userDTO));
        if(!user.getType().equals("ADMIN")){
            loginAttemptService.initializeLoginAttempts(user);
        }
        return userMapper.userToUserDTO(user);
    }
    @RequestMapping(value= "blocked/{id}", method = RequestMethod.PUT)
    @PreAuthorize("HasRole('Admin')")
    @ApiOperation(value = "unblock a user by id", authorizations = {@Authorization(value = "apiKey")})
    public void unblockUserById(@PathVariable Long id){
        loginAttemptService.unblockUserById(id);
    }

}
