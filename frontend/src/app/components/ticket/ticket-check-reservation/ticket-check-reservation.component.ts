import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../../services/auth/auth.service';

import {Customer} from '../../../dtos/customer';
import {Show} from '../../../dtos/show';
import {Seat} from '../../../dtos/seat';
import {PriceCategory} from '../../../dtos/priceCategory';
import {Sector} from '../../../dtos/sector';
import {Event} from '../../../dtos/event';
import {EventType} from '../../../datatype/event_type';
import {Artist} from '../../../dtos/artist';
import {Hall} from '../../../dtos/hall';
import {Location} from '../../../dtos/location';
import {TicketService} from '../../../services/ticket/ticket.service';
import {TicketStatus} from '../../../datatype/ticket_status';
import {TicketPost} from '../../../dtos/ticket-post';
import {Ticket} from '../../../dtos/ticket';

@Component({
  selector: 'app-ticket-check-reservation',
  templateUrl: './ticket-check-reservation.component.html',
  styleUrls: ['./ticket-check-reservation.component.scss']
})
export class TicketCheckReservationComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  submitted: boolean = false;

  private price: number[] = [];
  private priceTotal: number;
  private show: Show;
  private customer: Customer;
  private ticket_seats: Seat[] = [];
  private ticket_sectors: Sector[] = [];
  private tickets: TicketPost[] = [];
  private amtTickets: number;
  private seatsStr: String[] = [];
  private rowStr: String[] = [];
  private idxPrice: number;
  private ticketExistsError: boolean = false;
  private createdTickets: Ticket[];

  // TODO: delete default objects
  private sector: Sector;
  private event: Event;
  private hall: Hall;
  private artist: Artist;
  private location: Location;
  private seats: Seat[];
  private sectors: Sector[];
  // TODO: End of default objects

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig,
              private cd: ChangeDetectorRef, private authService: AuthService) {}

  ngOnInit() {
    // TODO: delete default objects
    this.location = new Location(9, 'Austria', 'Vienna', '1150', 'Roland-Rainer-Platz 1',
      'description for lcoation default test');
    this.artist = new Artist(8, 'Cher');
    this.hall = new Hall(7, 'Stadthalle A', this.location, this.seats, this.sectors);
    this.event = new Event(6, 'Here we go again', EventType.CONCERT, 'description for this default test event',
      'content for this default test event', this.artist, 120);
    this.sector = new Sector(5, 2, PriceCategory.Average);
    this.customer = new Customer(1, 'Müller', 'Petra', 'petra.mueller@email.com', '05.05.2005');
    this.show = new Show(2, this.event, '20:20', '12.05.2019', this.hall, 'description for this default test show', 44);
    this.ticket_seats.push(new Seat(12, 12, 22, PriceCategory.Average));
    this.ticket_seats.push(new Seat(10, 18, 22, PriceCategory.Average));
    this.price.push(35.99);
    this.price.push(35.99);
    // TODO: End of default objects

    this.idxPrice = 0;
    this.priceTotal = 0;

    for (const entry of this.price) {
      this.priceTotal += entry;
    }

    if (this.ticket_seats.length > 0) {
      this.amtTickets = this.ticket_seats.length;
      for (const entry of this.ticket_seats) {
        this.seatsStr.push(entry.seatNumber.toString());
        this.rowStr.push(entry.seatRow.toString());
      }
    }

    if (this.ticket_sectors.length > 0) {
      this.amtTickets = this.ticket_sectors.length;
    }
  }

  addTickets() {
    if (this.ticket_seats.length > 0) {
      this.amtTickets = this.ticket_seats.length;
      for (const entry of this.ticket_seats) {
        const currentTicket = new TicketPost(null, 2, 1, 44.56, 3, null, TicketStatus.RESERVATED);
        this.tickets.push(currentTicket);
      }
    }

    if (this.ticket_sectors.length > 0) {
      for (const entry of this.ticket_sectors) {
        const currentTicket = new TicketPost(null, 4, 7, this.price.pop(), null, 2, TicketStatus.RESERVATED);
        this.tickets.push(currentTicket);
      }
    }
    this.createTicket(this.tickets);
  }

  async createTicket(tickets: TicketPost[]) {
    this.ticketExistsError = false;
    this.addTicket(tickets);
    await this.delay(500);
    if (this.ticketExistsError) {
      console.log('Reservation already exists');
      return;
    } else {
      console.log('Here we gooo');
      this.submitted = true;
    }
  }

  getIdsOfCreatedTickets(): Number[] {
    const ticketIDs: Number[] = Number[this.createdTickets.length];
    for (let i = 0; i < this.createdTickets.length; i++) {
      ticketIDs[i] = this.createdTickets[i].id;
    }
    return ticketIDs;
  }

  /**
   * Sends news creation request
   * @param news the news which should be created
   */
  addTicket(tickets: TicketPost[]) {
    this.ticketService.createTicket(tickets).subscribe(
      (newTickets: Ticket[]) => {this.createdTickets = newTickets; },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Sends receipt request
   */
  getReceipt() {
    this.ticketService.getReceiptPdf(this.getIdsOfCreatedTickets()).subscribe(
      res => {
        const fileURL = URL.createObjectURL(res);
        window.open(fileURL, '_blank');
        },
        error => {
          console.log('receipt error')
          this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Sends ticket pdf request
  */
  getTicketPdf() {
    this.ticketService.getTicketPdf(this.getIdsOfCreatedTickets()).subscribe(
      /*(newTickets: Ticket[]) => {this.createdTickets = newTickets;},*/
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.ticketCheckReservation !== 'No message available') {
      this.errorMessage = error.error.ticketCheckReservation;
    } else {
      this.errorMessage = error.error.error;
    }
    if (error.error.status === 'BAD_REQUEST') {
      console.log('here we go');
      this.errorMessage = error.error.message;
      this.ticketExistsError = true;
    }
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
}
