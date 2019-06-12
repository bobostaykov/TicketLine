import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Ticket} from '../../dtos/ticket';
import {Observable} from 'rxjs';
import {Globals} from '../../global/globals';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all tickets from the backend
   */
  getAllTickets(): Observable<Ticket[]> {
    console.log('Get all tickets');
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri);
  }

  /**
   * Loads specific ticket from the backend
   * @param id of ticket to load
   */
  getTicketById(id: number): Observable<Ticket> {
    console.log('Load ticket for ' + id);
    return this.httpClient.get<Ticket>(this.ticketBaseUri + '/' + id);
  }

  /**
   * Persists ticket to the backend
   * @param ticket to persist
   */
  createTicket(ticket: Ticket) {
    console.log('Create ticket');
    return this.httpClient.post<Ticket>(this.ticketBaseUri, ticket);
  }
}
