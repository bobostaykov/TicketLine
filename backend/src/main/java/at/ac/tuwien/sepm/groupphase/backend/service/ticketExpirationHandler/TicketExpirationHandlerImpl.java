package at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TicketExpirationHandlerImpl implements TicketExpirationHandler {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final ShowMapper showMapper;

    public TicketExpirationHandlerImpl(TicketRepository ticketRepository, TicketMapper ticketMapper, ShowMapper showMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.showMapper = showMapper;
    }

    @Override
    public void setExpiredReservatedTicketsToStatusExpired(ShowDTO showDTO) {
        LocalDate showDate = showDTO.getDate();
        if (showDate.isAfter(LocalDate.now()))
            return;
        LocalTime showStartTime = showDTO.getTime();
        LocalTime localTimeAfter30Minutes = LocalTime.now().plusMinutes(30);
        if (showStartTime.isBefore(localTimeAfter30Minutes))
            return;
        List<TicketDTO> ticketsToExpire = ticketMapper.ticketToTicketDTO(ticketRepository.findAllByShowAndStatus(showMapper.showDTOToShow(showDTO), TicketStatus.RESERVATED));
        for (TicketDTO t:
             ticketsToExpire) {
            t.setStatus(TicketStatus.EXPIRED);
        }
        ticketRepository.saveAll(ticketMapper.ticketDTOToTicket(ticketsToExpire));
    }
}
