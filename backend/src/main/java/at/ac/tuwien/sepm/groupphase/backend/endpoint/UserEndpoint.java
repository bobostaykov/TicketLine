package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/messages/news-fetch")
@Api(value = "messages/news-fetch")
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Confirm news fetch", authorizations = {@Authorization(value = "apiKey")})
    public void updateUsersLatestNewsFetchDate(HttpServletRequest request) {
        System.out.println("INFO: updateUsersLatestNewsFetchDate for user: " + request.getUserPrincipal().getName());
        try {
            userService.addLatestNewsFetchDate(request.getUserPrincipal().getName(), LocalDateTime.now());
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
