import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Ticket} from '../../dtos/ticket';
import {Observable} from 'rxjs';
import {Globals} from '../../global/globals';
import {TicketPost} from '../../dtos/ticket-post';

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
  createTicket(ticket: TicketPost[]): Observable<Ticket[]> {
    console.log('Create ticket');
    return this.httpClient.post<Ticket[]>(this.ticketBaseUri, ticket);
  }

  /**
   * Requests ticket pdf creation for ticket(s) and receive it
   * @param ticketIDs IDs of the ticket(s) to request ticket pdf for
   */
  getTicketPdf(ticketIDs: Number[]): Observable<Blob> {
    console.log('Get ticket pdf for tickets: ' + ticketIDs);
    let params = new HttpParams();
    params = params.append('ticketIDs', ticketIDs.join(', '));
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    // tslint:disable-next-line:max-line-length
    return this.httpClient.get<Blob>(this.ticketBaseUri + '/' + 'receipt', {headers: headers, responseType: 'blob' as 'json', params: params});
  }

  /**
   * Requests receipt pdf creation for ticket(s) and receive it
   * @param ticketIDs IDs of the ticket(s) to request receipt pdf for
   */
  getReceiptPdf(ticketIDs: Number[]): Observable<Blob> {
    console.log('Get receipt pdf for tickets: ' + ticketIDs);
    let params = new HttpParams();
    params = params.append('ticketIDs', ticketIDs.join(', '));
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    // tslint:disable-next-line:max-line-length
    return this.httpClient.get<Blob>(this.ticketBaseUri + '/' + 'printable', {headers: headers, responseType: 'blob' as 'json', params: params});
  }
}
