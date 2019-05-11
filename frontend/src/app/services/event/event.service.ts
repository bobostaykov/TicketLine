import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {EventTickets} from '../../dtos/event_tickets';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get top ten events from backend
   */
  getTopTenEvents(monthsArray: string[], categoriesArray: string[]): Observable<EventTickets[]> {
    return this.httpClient.get<EventTickets[]>(this.eventBaseUri + '/topten?months='
                                              + monthsArray.join(',') + '&categories=' + categoriesArray.join(','));
  }

}
