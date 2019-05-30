import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Ticket} from '../../dtos/ticket';
import {TicketService} from '../../services/ticket.service';
import {AuthService} from '../../services/auth/auth.service';
import {CustomerService} from '../../services/customer.service';
import {Customer} from '../../dtos/customer';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent {

  error: boolean = false;
  errorMessage: string = '';
  ticketForm: FormGroup;
  submitted: boolean = false;
  private tickets: Ticket[];

  constructor(private authService: AuthService, private ticketService: TicketService,
              private customerService: CustomerService, private formBuilder: FormBuilder) {
    this.ticketForm = this.formBuilder.group({
      show: ['', [Validators.required]],
      price: ['', [Validators.required]],
      hall: ['', [Validators.required]],
      seatNumber: ['', [Validators.required]],
      seatRow: ['', [Validators.required]],
      sectorNumber: ['', [Validators.required]],
      status: ['', Validators.required]
    });
  }

  getTickets(): Ticket[] {
    return this.tickets;
  }

  /**
   * uses CustomerService to search for customers matching parameter
   * @param customer dto that includes search parameters for customerService
   */
  private searchCustomers(customer: Customer) {
    console.log('Queries customerService to search for customers resembling ' + JSON.stringify(customer));
    this.customerService.searchCustomers(customer).subscribe(
      () => {},
      error => this.defaultServiceErrorHandling(error)
    );
  }

  /**
   * Load all tickets from database
   */
  loadTickets() {
    this.ticketService.getAllTickets().subscribe(
      (tickets: Ticket[]) => { this.tickets = tickets; },
      error => { this.defaultServiceErrorHandling(error); }
    );
  }


  addTicketAndCustomer() {
    this.submitted = true;
    if (this.ticketForm.valid) {
      const customer: Customer = new Customer(null,
        this.ticketForm.controls.name.value,
        this.ticketForm.controls.firstname.value,
        this.ticketForm.controls.email.value,
        this.ticketForm.controls.birthday.value);
      this.customerService.createCustomer(customer);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Starts form validation and builds a ticket dto for sending a buy request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addTicket() {
    this.submitted = true;
    if (this.ticketForm.valid && (this.ticketForm.controls.type.value === 'ADMIN' || this.ticketForm.controls.type.value === 'SELLER')) {
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }


  /**
   * Sends ticket creation request
   * @param ticket the ticket which should be created
   */
  createTicket(ticket: Ticket) {
    this.ticketService.createTicket(ticket).subscribe(
      () => {},
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.message !== 'No message available') {
      this.errorMessage = error.error.message;
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
    this.ticketForm.reset();
    this.submitted = false;
  }

}
