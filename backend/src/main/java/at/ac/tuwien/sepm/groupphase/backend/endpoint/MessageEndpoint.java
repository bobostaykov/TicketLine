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

import java.util.List;

@RestController
@RequestMapping(value = "/messages")
@Api(value = "messages")
public class MessageEndpoint {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageEndpoint(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple message entries", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleMessageDTO> findAll() {
        return messageMapper.messageToSimpleMessageDTO(messageService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific message entry", authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDTO find(@PathVariable Long id) {
        return messageMapper.messageToDetailedMessageDTO(messageService.findOne(id));
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
