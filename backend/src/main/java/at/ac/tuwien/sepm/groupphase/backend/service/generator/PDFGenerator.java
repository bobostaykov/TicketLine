package at.ac.tuwien.sepm.groupphase.backend.service.generator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PDFGenerator {

    /**
     * Generates receipt PDF from List of TicketDTOs
     *
     * @param tickets List of Ticket DTOs
     * @param cancellation flag for creation of cancellation receipt
     * @return receipt PDF as MultipartFile
     */
    MultipartFile generateReceipt(List<TicketDTO> tickets, Boolean cancellation) throws DocumentException, IOException;

    /**
     * Generates PDF containing ticket(s) and/or reservations(s) from List of TicketDTOs
     *
     * @param tickets List of Ticket DTOs
     * @return PDF containg ticket(s) and/or reservations(s)
     */
    MultipartFile generateTicketPDF(List<TicketDTO> tickets) throws DocumentException, IOException;

}
