package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/*
@RestController
@RequestMapping(value = "/files")
@Api(value = "files")
public class FileEndpoint {

    private final FileService fileService;

    public FileEndpoint(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get file by id", authorizations = {@Authorization(value = "apiKey")})
    public MultipartFile find(@PathVariable Long id) {
        return fileService.find(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new messages entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDTO publishMessage(@RequestBody DetailedMessageDTO detailedMessageDTO) {
        Message message = messageMapper.detailedMessageDTOToMessage(detailedMessageDTO);
        message = messageService.publishMessage(message);
        return messageMapper.messageToDetailedMessageDTO(message);
    }

}
*/