import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../../services/auth/auth.service';

import {Customer} from '../../../dtos/customer';
import {Show} from '../../../dtos/show';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {TicketService} from '../../../services/ticket/ticket.service';
import {TicketStatus} from '../../../datatype/ticket_status';
import {TicketPost} from '../../../dtos/ticket-post';
import {Ticket} from '../../../dtos/ticket';
import {TicketSessionService} from '../../../services/ticket-session/ticket-session.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-ticket-check',
  templateUrl: './ticket-check.component.html',
  styleUrls: ['./ticket-check.component.scss']
})
export class TicketCheckReservationComponent implements OnInit {
  error: boolean = false;
  errorMessage: string = '';

  private ticket_seats: Seat[] = [];
  private ticket_sectors: Sector[] = [];
  private ticket_show: Show;
  private ticket_customer: Customer;
  private ticket_status: TicketStatus;
  private submitted: boolean;

  private priceTotal: number;

  private tickets: TicketPost[] = [];
  private createdTickets: Ticket[] = [];
  private seatsStr: String[] = [];
  private rowStr: String[] = [];
  private sectorStr: String [] = [];
  private statusStr: String;
  private ticketOrReservation: String;
  private idx: number;
  private amtTickets: number;

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig,
              private cd: ChangeDetectorRef, private authService: AuthService, private ticketSession: TicketSessionService,
              private router: Router) {}

  ngOnInit() {
    if (!(this.ticketSession.getTickets().length > 0 || this.ticketSession.getCustomer() || this.ticketSession.getShow() ||
    this.ticketSession.getTicketStatus())) {
      this.router.navigate(['http://localhost:4200/floorplan']);
    }
    this.ticket_seats = this.ticketSession.getSeatTickets();
    this.ticket_sectors = this.ticketSession.getSectorTickets();
    this.ticket_customer = this.ticketSession.getCustomer();
    this.ticket_show = this.ticketSession.getShow();
    this.ticket_status = this.ticketSession.getTicketStatus();
    this.submitted = false;

    if (this.ticket_status === TicketStatus.RESERVED) {
      this.statusStr = 'Reservation';
      this.ticketOrReservation = 'Reservation';
    } else {
      this.statusStr = 'Purchase';
      this.ticketOrReservation = 'Ticket';
    }
    this.idx = 0;
    this.priceTotal = this.ticketSession.getTotalPrice();
    if (this.ticket_seats && this.ticket_seats.length > 0) {
      this.amtTickets = this.ticket_seats.length;
      for (const entry of this.ticket_seats) {
        this.seatsStr.push(entry.seatNumber.toString());
        this.rowStr.push(entry.seatRow.toString());
      }
    }
    if (this.ticket_sectors && this.ticket_sectors.length > 0) {
      this.amtTickets = this.ticket_sectors.length;
      for (const entry of this.ticket_sectors) {
        this.sectorStr.push(entry.sectorNumber.toString());
      }
    }
  }

  /**
   * Create all tickets given as TicketPost list.
   * parameter tickets: tickets to be created
   */
  createTicket() {
    this.tickets = [];
    const customerId = this.ticket_customer ? this.ticket_customer.id : null;
    if (this.ticket_seats && this.ticket_seats.length > 0) {
      for (const entry of this.ticket_seats) {
        const currentTicket = new TicketPost(null, this.ticket_show.id, customerId, entry.price, entry.id, null,
          this.ticket_status);
        this.tickets.push(currentTicket);
      }
    }

    if (this.ticket_sectors && this.ticket_sectors.length > 0) {
      for (const entry of this.ticket_sectors) {
        const currentTicket = new TicketPost(null, this.ticket_show.id, customerId, entry.price, null, entry.id,
          this.ticket_status);
        this.tickets.push(currentTicket);
      }
    }

    this.vanishError();
    this.addTicket(this.tickets);
  }

  /**
   * Sends ticket(s) creation request
   * @param tickets the ticket(s) which should be created
   */
  addTicket(tickets: TicketPost[]) {
    if (tickets.length > 0) {
      this.ticketService.createTicket(tickets).subscribe(
        (newTickets: Ticket[]) => {
          this.createdTickets = newTickets;
          this.submitted = true;
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('No item in tickets');
    }
  }

  private getIdsOfCreatedTickets(): Number[] {
    const ticketIDs: Number[] = []; /*Number[this.createdTickets.length];*/
    for (let i = 0; i < this.createdTickets.length; i++) {
      ticketIDs[i] = this.createdTickets[i].id;
    }
    return ticketIDs;
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
        console.log('receipt error');
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Sends deletion and cancellation receipt request
   */
  getCancellationReceipt() {
    this.ticketService.getCancellationReceiptPdf(this.getIdsOfCreatedTickets()).subscribe(
      res => {
        const fileURL = URL.createObjectURL(res);
        window.open(fileURL, '_blank');
      },
      error => {
        console.log('cancellation receipt error');
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Sends ticket pdf request
   */
  getTicketPdf() {
    this.ticketService.getTicketPdf(this.getIdsOfCreatedTickets()).subscribe(
      res => {
        const fileURL = URL.createObjectURL(res);
        console.log(fileURL);
        window.open(fileURL, '_blank');
      },
      error => {
        console.log('ticket pdf error');
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
      this.errorMessage = error.error.message.replace('400 BAD_REQUEST \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'NOT_FOUND') {
      this.errorMessage = error.error.message.replace('404 NOT_FOUND \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'INTERNAL_SERVER_ERROR') {
      this.errorMessage = error.error.message.replace('404 INTERNAL_SERVER_ERROR \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    }
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
}
