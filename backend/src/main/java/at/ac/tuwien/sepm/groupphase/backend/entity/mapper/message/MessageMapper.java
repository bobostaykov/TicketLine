package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.message;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.DetailedMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.message.SimpleMessageDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MessageSummaryMapper.class)
public interface MessageMapper {

    Message detailedMessageDTOToMessage(DetailedMessageDTO detailedMessageDTO);

    DetailedMessageDTO messageToDetailedMessageDTO(Message one);

    List<SimpleMessageDTO> messageToSimpleMessageDTO(List<Message> all);

    @Mapping(source = "text", target = "summary", qualifiedBy = MessageSummaryMapper.MessageSummary.class)
    SimpleMessageDTO messageToSimpleMessageDTO(Message one);

}