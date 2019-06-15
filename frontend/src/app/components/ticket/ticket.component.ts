import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AuthService} from '../../services/auth/auth.service';

import {TicketService} from '../../services/ticket/ticket.service';
import {Ticket} from '../../dtos/ticket';
import {News} from '../../dtos/news';
import {Customer} from '../../dtos/customer';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent implements OnInit {

  error: boolean = false;
  errorMessage: string = '';
  ticketForm: FormGroup;
  submitted: boolean = false;
  private ticket: Ticket;

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService) {}

  ngOnInit() {
  }

  /**
   * adds ticket to backend
   * @param ticket to be added
   */
  private addTicket(ticket: Ticket) {
    console.log('Create ticket');
    this.ticketService.createTicket(ticket).subscribe(
      addedTicket => this.ticket = addedTicket,
      error => this.defaultServiceErrorHandling(error)
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
    if (error.error.ticket !== 'No message available') {
      this.errorMessage = error.error.ticket;
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
