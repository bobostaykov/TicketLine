import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {EventTickets} from '../../dtos/event_tickets';
import {ResultsFor} from '../../datatype/results_for';
import {Event} from '../../dtos/event';

// TODO public or default methods

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get top ten events from backend
   */
  public getTopTenEvents(monthsArray: string[], categoriesArray: string[]): Observable<EventTickets[]> {
    console.log('Get top 10 events');
    return this.httpClient.get<EventTickets[]>(this.eventBaseUri + '/topten',
      {params: { months: monthsArray.join(','), categories: categoriesArray.join(',') }});
  }

  /**
   * Get all events that apply to a specific eventType of search term (resultsFor: ARTIST, EVENT, LOCATION) from backend
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
