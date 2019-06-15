package at.ac.tuwien.sepm.groupphase.backend.service.generator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PDFGeneratorImpl implements PDFGenerator{

    private static final String RECEIPT_PATH = "receipt/";
    private static final String TICKET_PATH = "ticket/";
    private static final String TICKET_CHECK_URL = "https://ticketline.at/tickets/check/";

    @Value("${receipt.address}")
    private static String TICKETLINE_ADDRESS;

    public MultipartFile generateReceipt(List<TicketDTO> tickets, Boolean cancellation) throws DocumentException, IOException {
        if (tickets.size() < 1)
            throw new NotFoundException("Cannot create receipt for empty list of Tickets.");
        Double returnSum;
        Double sum = 0.0;
        Document receipt = new Document();
        String fileName, justFileName;
        if (cancellation)
            justFileName = "cancellation-receipt_" + LocalDateTime.now().toString() + ".pdf";
        else
            justFileName = "receipt_" + LocalDateTime.now().toString() + ".pdf";
        fileName = RECEIPT_PATH + justFileName;
        this.generatePathIfNotExists(RECEIPT_PATH);
        OutputStream outputStream = new FileOutputStream(fileName);
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
            PdfPCell number = new PdfPCell();
            number.setPhrase(new Phrase(i.toString(), font));
            table.addCell(number);
            PdfPCell item = new PdfPCell();
            item.setPhrase(new Phrase(t.getShow().getEvent().getName(), font));
            table.addCell(item);
            PdfPCell price = new PdfPCell();
            price.setPhrase(new Phrase(this.doubleToEuro(t.getPrice()), font));
            sum += t.getPrice();
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


        // TODO: Use TICKETLINE_ADDRESS from application.yml here
        Paragraph address = new Paragraph("Ticketline-Gasse 1a, 1010 Wien", font);
        receipt.add(address);

        receipt.close();
        //return new File(fileName);

        File file = new File(fileName);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartPDF = new MockMultipartFile(justFileName,
            file.getName(), "application/pdf", IOUtils.toByteArray(input));
        return multipartPDF;
    }

    @Override
    public MultipartFile generateTicketPDF(List<TicketDTO> tickets) throws DocumentException, IOException, NoSuchAlgorithmException {
        int numberOfTickets = tickets.size();
        if (numberOfTickets < 1)
            throw new NotFoundException("Cannot create pdf for empty list of Tickets.");
        Document pdf = new Document();
        String justFileName = "ticket_" + LocalDateTime.now().toString() + ".pdf";
        String fileName = TICKET_PATH + justFileName;

        this.generatePathIfNotExists(TICKET_PATH);

        OutputStream outputStream = new FileOutputStream(fileName);
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

        File file = new File(fileName);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartPDF = new MockMultipartFile(justFileName,
            file.getName(), "application/pdf", IOUtils.toByteArray(input));
        return multipartPDF;
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

    private void generatePathIfNotExists(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        if (!Files.exists(path))
            Files.createDirectories(path);
    }

    private void addTicketPage(TicketDTO ticket, Document doc) throws DocumentException, NoSuchAlgorithmException {
        Font headlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph headline = new Paragraph("TICKETLINE TICKET", headlineFont);
        doc.add(headline);
        doc.add(Chunk.NEWLINE);
        Paragraph date = new Paragraph("Ausstellungsdatum: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), font);
        doc.add(date);
        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);

        //int[] widths = {3, 6, 2};
        PdfPTable table = new PdfPTable(1);
        //table.setWidths(widths);

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
        if (ticket.getSectorNumber() != null) {
            PdfPCell sector = new PdfPCell();
            sector.setPhrase(new Phrase("Sektor: " + ticket.getSectorNumber().toString(), fontBold));
            table.addCell(sector);
        }
        else {
            PdfPCell seat = new PdfPCell();
            seat.setPhrase(new Phrase("Reihe: " + ticket.getRowNumber().toString() + "  Sitzplatz: " + ticket.getSeatNumber().toString(), fontBold));
            table.addCell(seat);
        }
        PdfPCell price = new PdfPCell();
        price.setPhrase(new Phrase("Preis: " + this.doubleToEuro(ticket.getPrice()), fontBold));
        table.addCell(price);
        PdfPCell qr = new PdfPCell();
        qr.setImage(generateQrCode(ticket));
        qr.setFixedHeight(200);
        table.addCell(qr);

        doc.add(table);
    }

    private String getLocationAddress(LocationDTO location) {
        return String.format("%s\n%s %s, %s", location.getStreet(), location.getPostalCode(), location.getCity(), location.getCountry());
    }

    private Image generateQrCode(TicketDTO ticket) throws BadElementException, NoSuchAlgorithmException {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(ticket.getId().toString().getBytes());
        String sha3_256hex = bytesToHex(digest);
        String toCode = TICKET_CHECK_URL + sha3_256hex; // TODO: use ticket number instead
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
