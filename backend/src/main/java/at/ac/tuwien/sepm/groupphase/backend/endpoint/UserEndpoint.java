package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.DetailedMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.SimpleMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.message.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/news-fetch")
@Api(value = "news-fetch")
public class UserLatestNewsFetchEndpoint {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public UserLatestNewsFetchEndpoint(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple message entries", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleMessageDTO> findAll() {
        return messageMapper.messageToSimpleMessageDTO(messageService.findAll());
    }


    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of latest simple message entries", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleMessageDTO> findLatest(HttpServletRequest request) {
        return messageMapper.messageToSimpleMessageDTO(messageService.findLatest(request.getUserPrincipal().getName()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific message entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDTO find(@PathVariable Long id) {
        return messageMapper.messageToDetailedMessageDTO(messageService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Confirm news fetch", authorizations = {@Authorization(value = "apiKey")})
    public void publishMessage(HttpServletRequest request) {
        Message message = messageMapper.detailedMessageDTOToMessage(detailedMessageDTO);
        message = messageService.publishMessage(message);
    }

}
