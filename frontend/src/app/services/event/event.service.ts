import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {EventTickets} from '../../dtos/event_tickets';
import {ResultsFor} from '../../datatype/results_for';
import {Event} from '../../dtos/event';
import {TopTenDetails} from '../../dtos/top-ten-details';

// TODO public or default methods

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Create event
   */
  public createEvent(event: Event): Observable<Event> {
    console.log('Create event');
    return this.httpClient.post<Event>(this.eventBaseUri, event);
  }

  /**
   * Get top ten events from backend
   */
  public getTopTenEvents(monthsCats: TopTenDetails): Observable<EventTickets[]> {
    console.log('Get top 10 events');
    return this.httpClient.post<EventTickets[]>(this.eventBaseUri + '/topten', monthsCats);
  }

  /**   * Get all events that apply to a specific eventType of search term (resultsFor: ARTIST, EVENT, LOCATION) from backend
   * If resultsFor === ResultsFor.LOCATION, name_or_id will be the location's id, otherwise it will be the name of the event/artist
   */
  /*
  public getEventsFiltered(resultsFor: ResultsFor, nameOrId: string): Observable<Event[]> {
    console.log('Get events filtered');
    return this.httpClient.get<Event[]>(this.eventBaseUri, {params: { results_for: ResultsFor[resultsFor], name_or_id: nameOrId }});
  }
  */

  /**
   * Get all events from backend
   */
  public getAllEvents(page: number): Observable<Event[]> {
    console.log('Get all events');
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/?page=' + page);
  }

}
