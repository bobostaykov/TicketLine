package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.userNews.UserNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.userNews.UserNewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.UserNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(value = "news-fetch")
@Api(value = "news-fetch")
public class UserNewsEndpoint {

    private final UserNewsService userNewsService;
    private final UserNewsMapper userNewsMapper;

    public UserNewsEndpoint(UserNewsService userNewsService, UserNewsMapper userNewsMapper) {
        this.userNewsService = userNewsService;
        this.userNewsMapper = userNewsMapper;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Confirm news fetch", authorizations = {@Authorization(value = "apiKey")})
    public void addNewsFetch(@RequestBody UserNewsDTO userNewsDTO) {
        try {
            userNewsService.addNewsFetch(userNewsMapper.userNewsDTOToUserNews(userNewsDTO));
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
