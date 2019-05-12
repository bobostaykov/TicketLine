package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
<<<<<<< HEAD
=======
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
>>>>>>> Implemented getFile and frontend file processing partially refs: #35076 and #35077
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;

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
        return newsService.findUnread(request.getUserPrincipal().getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedNewsDTO find(@PathVariable Long id) {
        return newsService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new news entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO) {
        return newsService.publishNews(detailedNewsDTO);
    }


}
