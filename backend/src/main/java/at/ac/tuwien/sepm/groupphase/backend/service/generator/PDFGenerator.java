package at.ac.tuwien.sepm.groupphase.backend.service.generator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import com.itextpdf.text.DocumentException;

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
    byte[] generateReceipt(List<TicketDTO> tickets, Boolean cancellation) throws IOException, DocumentException;

    /**
     * Generates PDF containing ticket(s) and/or reservations(s) from List of TicketDTOs
     *
     * @param tickets List of Ticket DTOs
     * @return PDF containg ticket(s) and/or reservations(s)
     */
    byte[] generateTicketPDF(List<TicketDTO> tickets) throws DocumentException, IOException, NoSuchAlgorithmException;

}
