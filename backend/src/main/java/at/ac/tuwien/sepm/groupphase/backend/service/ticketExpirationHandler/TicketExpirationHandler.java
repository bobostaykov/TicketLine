package at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TicketExpirationHandler {

    /**
     * Searchs for expired reservated tickets and sets status to EXPIRED
     *
     * @param showDTO Show to check and set expired reservated tickets to expired
     */
    void setExpiredReservatedTicketsToStatusExpired(ShowDTO showDTO);
}
