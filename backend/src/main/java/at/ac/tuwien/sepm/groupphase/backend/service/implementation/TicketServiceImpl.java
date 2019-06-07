package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CustomerService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final TicketMapper ticketMapper;
    private final ShowMapper showMapper;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    public TicketServiceImpl(TicketRepository ticketRepository, CustomerRepository customerRepository,
                             EventRepository eventRepository, TicketMapper ticketMapper, ShowMapper showMapper,
                             CustomerMapper customerMapper, CustomerService customerService,
                             ShowRepository showRepository) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
        this.ticketMapper = ticketMapper;
        this.showMapper = showMapper;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.showRepository = showRepository;

    }

    @Override
    public TicketDTO postTicket(TicketDTO ticketDTO) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.save(ticketMapper.ticketDTOToTicket(ticketDTO)));
    }

    @Override
    public List<TicketDTO> findAll() {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findAllByOrderByIdAsc());
    }

    @Override
    public TicketDTO findOne(Long id) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public TicketDTO changeStatusToSold(Long id) {
        TicketDTO ticket = this.findOneReservated(id);
        ticket.setStatus(TicketStatus.SOLD);
        return ticketMapper.ticketToTicketDTO(ticketRepository.save(ticketMapper.ticketDTOToTicket(ticket)));
    }

    @Override
    public TicketDTO findOneReservated(Long id) {
        return ticketMapper.ticketToTicketDTO(ticketRepository.findOneByIdAndStatus(id, TicketStatus.RESERVATED).orElseThrow(NotFoundException::new));
    }

    @Override
    @Transactional
    public TicketDTO deleteOne(Long id) {
        Optional<Ticket> ticket = ticketRepository.findOneById(id);
        if (ticket.isPresent()) {
            ticketRepository.delete(ticket.get());
        }
        else
            throw new NotFoundException("Ticket with id " + id + " not found.");
        return ticketMapper.ticketToTicketDTO(ticket.get());
    }

    @Override
    public List<TicketDTO> findAllFilteredByCustomerAndEvent(String customerName, String eventName) {
        List<Customer> customers = customerRepository.findAllByName(customerName);
        List<Event> events =  eventRepository.findAllByName(eventName);
        List<Show> shows = showRepository.findAllByEvent(events);
        List<Ticket> result1 = new ArrayList<>();
        List<Ticket> result2 = new ArrayList<>();
        if (customerName != null) {
            result1 = ticketRepository.findAllByCustomer(customers);
        }
        if (eventName != null) {
            result2 = ticketRepository.findAllByShow(shows);
        }
        return ticketMapper.ticketToTicketDTO(this.difference(result1, result2));
    }

    @Override
    public MultipartFile getReceipt(List<String> ticketIDs) throws Exception {
        List<TicketDTO> tickets = new ArrayList<>();
        for (String id:ticketIDs) {
            tickets.add(ticketMapper.ticketToTicketDTO(ticketRepository.findOneById(Long.parseLong(id)).orElseThrow(NotFoundException::new)));
        }
        return this.generateReceipt(tickets);
    }

    /**
     * Generates receipt PDF from List of TicketDTOs
     *
     * @param tickets List of Ticket DTOs
     * @return receipt PDF as MultipartFile
     */
    private MultipartFile generateReceipt(List<TicketDTO> tickets) throws FileNotFoundException, DocumentException {
        int numberOfTickets = tickets.size();
        Double sum = 0.0;
        Document receipt = new Document();
        String fileName = "receipt_" + LocalDateTime.now().toString() + ".pdf";
        PdfWriter.getInstance(receipt, new FileOutputStream(fileName));

        receipt.open();
        Font headlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Chunk headline = new Chunk("TICKETLINE RECHNUNG", headlineFont);
        receipt.add(headline);

        PdfPTable table = new PdfPTable(2);

        PdfPCell itemTitle = new PdfPCell();
        itemTitle.setPhrase(new Phrase("Position", fontBold));
        table.addCell(itemTitle);
        PdfPCell priceTitle = new PdfPCell();
        priceTitle.setPhrase(new Phrase("Preis", fontBold));
        table.addCell(priceTitle);

        for (TicketDTO t:tickets) {
            PdfPCell item = new PdfPCell();
            item.setPhrase(new Phrase(t.getShow().getEvent().getName(), font));
            table.addCell(item);
            PdfPCell price = new PdfPCell();
            price.setPhrase(new Phrase(this.print(t.getPrice()), font));
            sum += t.getPrice();
            table.addCell(price);
        }

        PdfPCell sumText = new PdfPCell();
        sumText.setPhrase(new Phrase("Summe", fontBold));
        table.addCell(sumText);
        PdfPCell sumValue = new PdfPCell();
        sumValue.setPhrase(new Phrase(this.print(sum), fontBold));
        table.addCell(sumValue);
        PdfPCell taxText = new PdfPCell();
        taxText.setPhrase(new Phrase("Steuersatz", fontBold));
        table.addCell(taxText);
        PdfPCell taxValue = new PdfPCell();
        taxValue.setPhrase(new Phrase("13%", fontBold));
        table.addCell(taxValue);

        receipt.add(table);
        receipt.close();
        return null;
    }

    /**
     * Generates Currency (€) String from Double
     *
     * @param price as Double
     * @return String representation as €
     */
    private String print(Double price) {
        long eurocents = Math.round(price * 100);
        String centsStr = Long.toString(100 + eurocents%100).substring(1);
        return eurocents / 100 + "," + centsStr + " €";
    }

    /**
     * Get the conjunction of tickets in list 1 and list 2
     *
     * @param list1 list of tickets
     * @param list2 list of tickets
     * @return conjunction of list1 and list2
     */
    private List<Ticket> difference(List<Ticket> list1, List<Ticket> list2) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : list1) {
            if(list2.contains(ticket)) {
                result.add(ticket);
            }
        }
        return result;
    }

    // PINOS IMPLEMENTATION
    /*
    @Override
    public List<TicketDTO> findByCustomerNameAndShowWithStatusReservated(String surname, String firstname, ShowDTO showDTO) {
        List<CustomerDTO> customers = customerService.findCustomersFiltered(null, surname, firstname, null, null);
        if (customers.isEmpty())
            throw new NotFoundException("No Customer with name " + firstname + surname + " found.");
        List<TicketDTO> tickets = new ArrayList<>();
        for (CustomerDTO c: customers) {
            List<Ticket> ticketsTemp = ticketRepository.findAllByCustomerAndShowWithStatusReservated(customerMapper.customerDTOToCustomer(c), showMapper.showDTOToShow(showDTO));
            for (Ticket t: ticketsTemp) {
                tickets.add(ticketMapper.ticketToTicketDTO(t));
            }
        }
        return tickets;
    }
    */

}
