package at.ac.tuwien.sepm.groupphase.backend.service.generator;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PDFGeneratorImpl implements PDFGenerator{

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorImpl.class);

    @Value("${receipt.address}")
    private String TICKETLINE_ADDRESS;

    @Value("${ticket.check.url}")
    private String TICKET_CHECK_URL;

    public byte[] generateReceipt(List<TicketDTO> tickets, Boolean cancellation) throws DocumentException {
        LOGGER.info("PDF Generator: Generate (cancellation) receipt for ticket(s)");
        if (tickets.size() < 1)
            throw new NotFoundException("Cannot create receipt for empty list of Tickets.");
        Double returnSum;
        Double sum = 0.0;
        Document receipt = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(receipt, outputStream);

        receipt.addTitle("Ticketline" + (cancellation ? " cancellation" : "") + " receipt");
        receipt.addAuthor("Ticketline");
        receipt.addSubject("Ticketline" + (cancellation ? " cancellation" : "") + " receipt");
        receipt.addKeywords("Ticketline");
        receipt.addCreator("Ticketline");

        receipt.open();
        Font headlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph headline;
        if (cancellation)
            headline = new Paragraph("TICKETLINE STORNO-RECHNUNG", headlineFont);
        else
            headline = new Paragraph("TICKETLINE RECHNUNG", headlineFont);
        receipt.add(headline);
        receipt.add(Chunk.NEWLINE);
        Paragraph date = new Paragraph("Rechnungsdatum: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), font);
        receipt.add(date);
        receipt.add(Chunk.NEWLINE);
        receipt.add(Chunk.NEWLINE);

        int[] widths = {3, 6, 2};
        PdfPTable table = new PdfPTable(3);
        table.setWidths(widths);

        PdfPCell itemNumber = new PdfPCell();
        itemNumber.setPhrase(new Phrase("Position", fontBold));
        table.addCell(itemNumber);
        PdfPCell itemTitle = new PdfPCell();
        itemTitle.setPhrase(new Phrase("Bezeichnung", fontBold));
        table.addCell(itemTitle);
        PdfPCell priceTitle = new PdfPCell();
        priceTitle.setPhrase(new Phrase("Preis", fontBold));
        table.addCell(priceTitle);

        Integer i = 1;
        for (TicketDTO t:tickets) {
            if(t.getStatus() == TicketStatus.EXPIRED)
                continue;
            PdfPCell number = new PdfPCell();
            number.setPhrase(new Phrase(i.toString(), font));
            table.addCell(number);
            PdfPCell item = new PdfPCell();
            item.setPhrase(new Phrase(t.getShow().getEvent().getName() + (t.getStatus() == TicketStatus.RESERVED ? " - Reservierung" : ""), font));
            table.addCell(item);
            PdfPCell price = new PdfPCell();
            price.setPhrase(new Phrase(this.doubleToEuro(t.getStatus() == TicketStatus.SOLD ? t.getPrice() : 0.0), font));
            sum += t.getStatus() == TicketStatus.SOLD ? t.getPrice() : 0;
            table.addCell(price);
            i++;
        }

        PdfPCell blank = new PdfPCell();
        blank.setPhrase(new Phrase(" ", font));

        if (cancellation) {
            returnSum = - sum;
            sum = 0.0;
            table.addCell(blank);
            PdfPCell sumText = new PdfPCell();
            sumText.setPhrase(new Phrase("Rückgegeben in Bar", fontBold));
            table.addCell(sumText);
            PdfPCell sumValue = new PdfPCell();
            sumValue.setPhrase(new Phrase(this.doubleToEuro(returnSum), fontBold));
            table.addCell(sumValue);
        }
        table.addCell(blank);
        PdfPCell sumText = new PdfPCell();
        sumText.setPhrase(new Phrase("Summe", fontBold));
        table.addCell(sumText);
        PdfPCell sumValue = new PdfPCell();
        sumValue.setPhrase(new Phrase(this.doubleToEuro(sum), fontBold));
        table.addCell(sumValue);
        table.addCell(blank);
        PdfPCell taxText = new PdfPCell();
        taxText.setPhrase(new Phrase("Steuersatz", fontBold));
        table.addCell(taxText);
        PdfPCell taxValue = new PdfPCell();
        taxValue.setPhrase(new Phrase("13%", fontBold));
        table.addCell(taxValue);

        receipt.add(table);

        receipt.add(Chunk.NEWLINE);
        receipt.add(Chunk.NEWLINE);

        Paragraph address = new Paragraph(TICKETLINE_ADDRESS, font);
        receipt.add(address);

        receipt.close();

        return outputStream.toByteArray();
    }

    @Override
    public byte[] generateTicketPDF(List<TicketDTO> tickets) throws DocumentException {
        LOGGER.info("PDF Generator: Generate PDF for ticket(s) and/or reservation(s)");
        int numberOfTickets = tickets.size();
        if (numberOfTickets < 1)
            throw new NotFoundException("Cannot create pdf for empty list of Tickets.");

        Document pdf = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, outputStream);

        pdf.addTitle("Ticketline Ticket");
        pdf.addAuthor("Ticketline");
        pdf.addSubject("Ticketline Ticket");
        pdf.addKeywords("Ticketline");
        pdf.addCreator("Ticketline");

        pdf.open();

        for (TicketDTO ticket:
             tickets) {
            this.addTicketPage(ticket, pdf);
            pdf.newPage();
        }

        pdf.close();
        return outputStream.toByteArray();
    }

    /**
     * Generates Currency (€) String from Double
     *
     * @param price as Double
     * @return String representation as €
     */
    private String doubleToEuro(Double price) {
        String plusMinus = "";
        if (price < 0.0) {
            price = -price;
            plusMinus = "- ";
        }
        long eurocents = Math.round(price * 100);
        String centsStr = Long.toString(100 + eurocents%100).substring(1);
        return plusMinus + eurocents / 100 + "," + centsStr + " €";
    }

    private void addTicketPage(TicketDTO ticket, Document doc) throws DocumentException {
        Boolean buy = ticket.getStatus() == TicketStatus.SOLD;
        Font headlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph headline = new Paragraph("TICKETLINE " + (ticket.getStatus() == TicketStatus.SOLD ? "TICKET" : "RESERVIERUNG"), headlineFont);
        doc.add(headline);
        doc.add(Chunk.NEWLINE);
        Paragraph date = new Paragraph("Ausstellungsdatum: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), font);
        doc.add(date);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(1);

        PdfPCell eventName = new PdfPCell();
        eventName.setPhrase(new Phrase(ticket.getShow().getEvent().getName(), fontBold));
        table.addCell(eventName);
        PdfPCell showName = new PdfPCell();
        showName.setPhrase(new Phrase(ticket.getShow().getHall().getLocation().getLocationName(), fontBold));
        table.addCell(showName);
        PdfPCell showAddress = new PdfPCell();
        showAddress.setPhrase(new Phrase(this.getLocationAddress(ticket.getShow().getHall().getLocation()), fontBold));
        table.addCell(showAddress);
        PdfPCell showDate = new PdfPCell();
        showDate.setPhrase(new Phrase(ticket.getShow().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " +
            ticket.getShow().getTime().format(DateTimeFormatter.ofPattern("HH:mm")), fontBold));
        table.addCell(showDate);
        if (ticket.getSector() != null && ticket.getSector().getSectorNumber() != null) {
            PdfPCell sector = new PdfPCell();
            sector.setPhrase(new Phrase("Sektor: " + ticket.getSector().getSectorNumber().toString(), fontBold));
            table.addCell(sector);
        }
        else {
            PdfPCell seat = new PdfPCell();
            seat.setPhrase(new Phrase("Reihe: " + ticket.getSeat().getSeatRow().toString() + "  Sitzplatz: " + ticket.getSeat().getSeatNumber().toString(), fontBold));
            table.addCell(seat);
        }
        PdfPCell price = new PdfPCell();
        if (buy) {
            price.setPhrase(new Phrase("Preis: " + this.doubleToEuro(ticket.getPrice()), fontBold));
            table.addCell(price);
            PdfPCell qr = new PdfPCell();
            qr.setImage(generateQrCode(ticket));
            qr.setFixedHeight(200);
            table.addCell(qr);
        }
        else {
            price.setPhrase(new Phrase("Preis (zu bezahlen): " + this.doubleToEuro(ticket.getPrice()), fontBold));
            table.addCell(price);
            PdfPCell reservationNumber = new PdfPCell();
            reservationNumber.setPhrase(new Phrase("Reservierungsnummer: \n" + ticket.getReservationNo(), fontBold));
            table.addCell(reservationNumber);
        }

        doc.add(table);
    }

    private String getLocationAddress(LocationDTO location) {
        return String.format("%s\n%s %s, %s", location.getStreet(), location.getPostalCode(), location.getCity(), location.getCountry());
    }

    private Image generateQrCode(TicketDTO ticket) throws BadElementException {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(ticket.getReservationNo().getBytes());
        String sha3_256hex = bytesToHex(digest);
        String toCode = TICKET_CHECK_URL + sha3_256hex;
        BarcodeQRCode qr = new BarcodeQRCode(toCode, 1000, 1000, null);
        Image image = qr.getImage();
        image.scaleAbsolute(250, 250);
        return image;
    }

    private static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
