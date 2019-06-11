package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEndpoint.class);

    private final FileService fileService;

    private static final String FILE_URI = "/file";
    private static final String FORBIDDEN_FILE_TYPE_MESSAGE = "Could not post file: forbidden file type ";
    private static final String ALLOWED_FILE_TYPES = "jpg, jpeg, png";

    public NewsEndpoint(NewsService newsService, FileService fileService) {
        this.newsService = newsService;
        this.fileService = fileService;
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

    @RequestMapping(value = FILE_URI + "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get file by id", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<Resource> findFileById(@PathVariable Long id) {
        File file = fileService.getFile(id);
        ResponseEntity<Resource>  returnValue = ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("image/" + file.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
            .body(new ByteArrayResource((file.getData())));
        return returnValue;
    }

    @RequestMapping(value = FILE_URI, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Store file", authorizations = {@Authorization(value = "apiKey")})
    public String storeFile(@RequestParam("file") MultipartFile file) {

        if (ALLOWED_FILE_TYPES.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {
            try {
                Long returnValue = fileService.storeFile(file);
                return returnValue.toString();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN_FILE_TYPE_MESSAGE + FilenameUtils.getExtension(file.getOriginalFilename()));
        }
    }

}
