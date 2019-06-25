import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Ticket} from '../../dtos/ticket';
import {Observable} from 'rxjs';
import {Globals} from '../../global/globals';
import {TicketPost} from '../../dtos/ticket-post';
import {Tick} from 'ng5-slider/slider.component';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';
  private printableTicketBaseUri: string = this.ticketBaseUri + '/printable';
  private ticketBuyUri: string = this.ticketBaseUri + '/buy';

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
   *
   * @param number the reservation Number
   * @param page the requested page
   */
  getReservationsByNumber(number: string, page: number): Observable<Ticket>{
    console.log('Load reservations by number: ' + number);
    return this.httpClient.get<Ticket>(this.ticketBaseUri + '/filter/?number=' + number + '&page=' + page);
  }

  /**
   * gets sold tickets by number
   * @param number the number that is searched for
   * @param page the page
   */
  getTicketsByNumber(number: string, reserved: boolean , page: number): Observable<Ticket>{
    console.log('Load tickets by number: ' + number + ' reserved= ' + + reserved);
    return this.httpClient.get<Ticket>(this.ticketBaseUri + '/filter/?number=' + number + '&page=' + page + '&reserved=' + reserved);
  }
  /**
   * searches for reservations by last name of the customer and event
   * @param customer last name of the customer
   * @param event name of the event
   * @param page the page
   */
  getReservedTicketsByConsumerAndEvent(customer: string, event: string, page: number): Observable<Ticket>{
    console.log('load reserved tickets by consumer ' + customer + 'and event ' + event);
    console.log(this.ticketBaseUri + '/filter/?customerName=' + customer + '&eventName=' + event + '&page=' + page /*+
      '&reserved=true'*/);
    return this.httpClient.get<Ticket>(this.ticketBaseUri + '/filter/?customerName=' + customer + '&eventName=' + event + '&page=' + page +
      '&reserved=true');
  }

  /**
   * Persists ticket to the backend
   * @param ticket to persist
   */
  createTicket(ticket: TicketPost[]): Observable<Ticket[]> {
    console.log('Create ticket');
    return this.httpClient.post<Ticket[]>(this.ticketBaseUri, ticket);
  }

  buyReservedTickets(ticketIDs: Number[]): Observable<Ticket> {
    console.log('sell reservated tickets for tickets' + ticketIDs);
     return this.httpClient.post<Ticket>(this.ticketBuyUri, ticketIDs);
  }

  /**
   * Requests ticket pdf creation for ticket(s) and receive it
   * @param ticketIDs IDs of the ticket(s) to request ticket pdf for
   */
  getTicketPdf(ticketIDs: Number[]): Observable<Blob> {
    console.log('Get ticket pdf for tickets: ' + ticketIDs);
    let params = new HttpParams();
    params = params.append('tickets', ticketIDs.toString());
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    // tslint:disable-next-line:max-line-length
    return this.httpClient.get<Blob>(this.printableTicketBaseUri + '/' + 'ticket', {headers: headers, responseType: 'blob' as 'json', params: params});
  }

  /**
   * Requests receipt pdf creation for ticket(s) and receive it
   * @param ticketIDs IDs of the ticket(s) to request receipt pdf for
   */
  getReceiptPdf(ticketIDs: Number[]): Observable<Blob> {
    console.log('Get receipt pdf for tickets: ' + ticketIDs);
    let params = new HttpParams();
    params = params.append('tickets', ticketIDs.toString());
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    // tslint:disable-next-line:max-line-length
    return this.httpClient.get<Blob>(this.printableTicketBaseUri + '/' + 'receipt', {headers: headers, responseType: 'blob' as 'json', params: params});
  }

  /**
   * Requests deletion and cancellation receipt pdf creation for ticket(s) and receive it
   * @param ticketIDs IDs of the ticket(s) to delete and request receipt pdf for
   */
  getCancellationReceiptPdf(ticketIDs: Number[]): Observable<Blob> {
    console.log('Delete and get cancellation receipt pdf for tickets: ' + ticketIDs);
    let params = new HttpParams();
    params = params.append('tickets', ticketIDs.toString());
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    // tslint:disable-next-line:max-line-length
    return this.httpClient.delete<Blob>(this.printableTicketBaseUri + '/' + 'cancellation', {headers: headers, responseType: 'blob' as 'json', params: params});
  }
}
