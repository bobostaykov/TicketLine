package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public Page<UserDTO> findAll(@RequestParam(value = "page") Integer page) {
        LOGGER.info("Get all users");
        try {
            return userService.findAll(page);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get a user by id", authorizations = {@Authorization(value = "apiKey")})
    public UserDTO find(@PathVariable Long id) {
        LOGGER.info("Get user with id " + id);
        try {
            return userService.findOne(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a user", authorizations = {@Authorization(value = "apiKey")})
    public UserDTO create(@RequestBody UserDTO userDTO) {
        LOGGER.info("Post user " + userDTO.getUsername());
        try {
            return userService.createUser(userDTO);
        } catch (ServiceException e) {
            if (e.getCause() instanceof DataIntegrityViolationException) {
                return UserDTO.builder().id(-1L).build();
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete a user by id", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable Long id) {
        LOGGER.info("Delete user with id " + id);
        try {
            userService.deleteUser(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        LOGGER.info("\n\n\n" + id + "\n\n\n");
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
    public Page<UserDTO> getAllBlockedUsers(@RequestParam(value = "page", required = false) Integer page){
        LOGGER.info("get all blocked users");
        try{
            return userService.getAllBlockedUsers(page);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during reading filtered customers", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for events by that artist: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No events are found by that artist:" + e.getMessage(), e);
        }
    }
}
