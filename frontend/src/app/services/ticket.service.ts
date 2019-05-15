import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Ticket} from '../dtos/ticket';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all tickets from database
   */
  getAllTickets(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri);
  }

  /**
   * Persists customer to the backend
   * @param customer to persist
   */
  createTicket(ticket: Ticket): Observable<Ticket> {
    console.log('Create ticket');
    return this.httpClient.post<Ticket>(this.ticketBaseUri, ticket);
  }
}
