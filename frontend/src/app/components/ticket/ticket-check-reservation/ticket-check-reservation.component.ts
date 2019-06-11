import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup} from '@angular/forms';
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
import {Ticket} from '../../../dtos/ticket';
import {TicketService} from '../../../services/ticket/ticket.service';

@Component({
  selector: 'app-ticket-check-reservation',
  templateUrl: './ticket-check-reservation.component.html',
  styleUrls: ['./ticket-check-reservation.component.scss']
})
export class TicketCheckReservationComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  ticketCheckReservationForm: FormGroup;
  submitted: boolean = false;
  // TODO: delete default objects
  private sector: Sector;
  private event: Event;
  private hall: Hall;
  private artist: Artist;
  private location: Location;
  private seats: Seat[];
  private sectors: Sector[];
  // TODO: End of default objects
  private price: number[] = [];
  private priceTotal: number;
  private show: Show;
  private customer: Customer;
  private ticket_seats: Seat[] = [];
  private ticket_sectors: Sector[] = [];
  private tickets: Ticket[] = [];
  private amtTickets: number;
  private seatsStr: String[] = [];
  private rowStr: String[] = [];
  private idxPrice: number;

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService) {}

  ngOnInit() {
    // TODO: delete default objects
    this.location = new Location(9, 'Austria', 'Vienna', '1150', 'Roland-Rainer-Platz 1',
      'description for lcoation default test');
    this.artist = new Artist(8, 'Cher');
    this.hall = new Hall(7, 'Stadthalle A', this.location, this.seats, this.sectors);
    this.event = new Event(6, 'Here we go again', EventType.CONCERT, 'description for this default test event',
      'content for this default test event', this.artist);
    this.sector = new Sector(5, 2, PriceCategory.Average);
    this.customer = new Customer(3, 'Müller', 'Petra', 'petra.mueller@email.com', '05.05.2005');
    this.show = new Show(2, this.event, '20:20', '12.05.2019', this.hall, 'description for this default test show', 44);
    this.ticket_seats.push(new Seat(10, 14, 22, PriceCategory.Average));
    this.ticket_seats.push(new Seat(10, 15, 22, PriceCategory.Average));
    this.price.push(35.99);
    this.price.push(35.99);
    // TODO: End of default objects
    this.idxPrice = 0;
    this.priceTotal = 0;

    if (this.ticket_seats.length > 0) {
      this.amtTickets = this.ticket_seats.length;
      for (const entry of this.ticket_seats) {
        this.seatsStr.push(entry.seatNumber.toString());
        this.rowStr.push(entry.seatRow.toString());
        this.tickets.push(new Ticket(null, this.show, this.customer, this.price[this.idxPrice], entry, null, 'reservation'));
        this.idxPrice += 1;
        this.priceTotal += this.price.pop();
      }
    }

    if (this.ticket_sectors.length > 0) {
      this.amtTickets = this.ticket_sectors.length;
      for (const entry of this.ticket_sectors) {
        this.tickets.push(new Ticket(null, this.show, this.customer, this.price[this.idxPrice], null, entry, 'reservation'));
        this.idxPrice += 1;
        this.priceTotal += this.price.pop();
      }
    }
  }

  /**
   * Starts form validation and builds a news dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  createTicket() {
    this.submitted = true;
    for (const entry of this.tickets) {
      this.addTicket(entry);
    }
  }

  /**
   * Sends news creation request
   * @param news the news which should be created
   */
  addTicket(ticket: Ticket) {
    this.ticketService.createTicket(ticket).subscribe(
      () => {},
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
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.ticketCheckReservationForm.reset();
    this.submitted = false;
  }

}
