package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PersistenceException;
import java.net.http.HttpClient;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Api(value = "users")
public class UserEndpoint {

    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public UserEndpoint(UserService userService) {
        this.userService = userService;
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
        try {
            return userService.createUser(userDTO);
        } catch (ServiceException e) {
            if (e.getCause() instanceof DataIntegrityViolationException) {
                return UserDTO.builder().id(-1L).build();
            }
            // TODO
            return null;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    @PreAuthorize("hasRole('ADMIN')")

    @ApiOperation(value = "Delete a user by id", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable Long id) {
        LOGGER.info("\n\n\n" + id + "\n\n\n");
        userService.deleteUser(id);
    }

    @RequestMapping(value = "/blocked/{id}" ,method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "block user by id", authorizations = {@Authorization(value = "apiKey")})
    public boolean blockUser(@PathVariable Long id){
        LOGGER.info("blocking user with id" + id);
        try {
            return userService.blockUser(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error during blocking user with id: " + id +" "+ e.getMessage());
        }
    }
    @RequestMapping(value = "blocked/unblock/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "unblock user by id", authorizations = {@Authorization(value = "apiKey")})
    public boolean unblockUser(@PathVariable Long id){
        LOGGER.info("unblocking user with id: " + id);
        return userService.unblockUser(id);
    }

    @RequestMapping(value = "/blocked", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all blocked users", authorizations = {@Authorization(value = "apiKey")})
    public List<UserDTO> getAllBlockedUsers(){
        return userService.getAllBlockedUsers();
    }


}
