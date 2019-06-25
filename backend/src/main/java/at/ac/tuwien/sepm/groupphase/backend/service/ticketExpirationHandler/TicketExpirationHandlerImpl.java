package at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketExpirationHandlerImpl implements TicketExpirationHandler {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final ShowMapper showMapper;
    private final ShowRepository showRepository;

    public TicketExpirationHandlerImpl(TicketRepository ticketRepository, TicketMapper ticketMapper, ShowMapper showMapper,
                                       ShowRepository showRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.showMapper = showMapper;
        this.showRepository = showRepository;
    }

    @Override
    public void setExpiredReservatedTicketsToStatusExpiredForSpecificShow(ShowDTO showDTO) {
        if(!this.checkIfShowStartsInLessThan30Minutes(showDTO))
            return;
        List<TicketDTO> ticketsToExpire = ticketMapper.ticketToTicketDTO(ticketRepository.findAllByShowAndStatus(showMapper.showDTOToShow(showDTO), TicketStatus.RESERVED));
        for (TicketDTO t:
             ticketsToExpire) {
            if(t.getStatus() == TicketStatus.RESERVED)
                t.setStatus(TicketStatus.EXPIRED);
        }
        ticketRepository.saveAll(ticketMapper.ticketDTOToTicket(ticketsToExpire));
    }

    @Override
    public TicketDTO setExpiredReservedTicketsToStatusExpired(TicketDTO ticket) {
        return this.processSingleTicket(ticket);
    }

    @Override
    public List<TicketDTO> setExpiredReservedTicketsToStatusExpired(List<TicketDTO> tickets) {
        List<TicketDTO> result = new ArrayList<>();
        for (TicketDTO t: tickets) {
            result.add(this.processSingleTicket(t));
        }
        return result;
    }

    @Override
    public void setAllExpiredReservedTicketsToStatusExpired() {
        List<ShowDTO> shows = showMapper.showToShowDTO(showRepository.findAll());
        for (ShowDTO s: shows) {
            this.setExpiredReservatedTicketsToStatusExpiredForSpecificShow(s);
        }
    }

    private TicketDTO processSingleTicket(TicketDTO ticket) {
        if(ticket.getStatus() == TicketStatus.SOLD || !this.checkIfShowStartsInLessThan30Minutes(ticket.getShow()))
            return ticket;
        ticket.setStatus(TicketStatus.EXPIRED);
        ticketMapper.ticketToTicketDTO(ticketRepository.save(ticketMapper.ticketDTOToTicket(ticket)));
        return ticket;
    }

    private boolean checkIfShowStartsInLessThan30Minutes(ShowDTO show) {
        LocalDate showDate = show.getDate();
        if (showDate.isAfter(LocalDate.now()))
            return false;
        LocalTime showStartTime = show.getTime();
        LocalTime localTimeAfter30Minutes = LocalTime.now().plusMinutes(30);
        if (showStartTime.isAfter(localTimeAfter30Minutes))
            return false;
        return true;
    }
}
