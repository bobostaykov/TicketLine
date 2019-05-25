package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEndpoint.class);

    public NewsEndpoint(NewsService newsService) {
        this.newsService = newsService;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleNewsDTO> findAll() {
        return newsService.findAll();
    }


    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of unread News articles", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleNewsDTO> findUnread(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return newsService.findUnread(username);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedNewsDTO find(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return newsService.findOne(id, username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new news entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO) {
        return newsService.publishNews(detailedNewsDTO);
    }


}
