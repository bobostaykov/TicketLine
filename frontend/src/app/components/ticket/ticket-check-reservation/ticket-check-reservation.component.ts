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
  private ticket: Ticket;
  private show: Show;
  private customer: Customer;
  private seat: Seat;
  private sector: Sector;
  private event: Event;
  private hall: Hall;
  private artist: Artist;
  private location: Location;
  private seats: Seat[];
  private sectors: Sector[];

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService) {}

  ngOnInit() {
    // TODO: delete default objects
    this.location = new Location(9, 'Austria', 'Vienna', '1180', 'testStreet',
      'description for lcoation default test');
    this.artist = new Artist(8, 'ArtistTestName');
    this.hall = new Hall(7, 'HallTestName', this.location, this.seats, this.sectors);
    this.event = new Event(6, 'eventTestName', EventType.CONCERT, 'description for this default test event',
      'content for thie default test event', this.artist);
    this.sector = new Sector(5, 2, PriceCategory.Average);
    this.seat = new Seat(4, 15, 33, PriceCategory.Average);
    this.customer = new Customer(3, 'TestName', 'TestFirstName', 'Test@email.com', '05.05.2005');
    this.show = new Show(2, this.event, '20:20', '12.05.2019', this.hall, 'description for this default test show', 44);
    this.ticket = new Ticket(1, this.show, this.customer, 22.22, this.seat, this.sector, 'reservation');
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
