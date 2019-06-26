import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../../../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventResultsService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Finds all events in which the given artist takes part
   * @param id of the artist to look for
   * @param page the number of the page to return
   */
  public findEventsFilteredByArtistID(id, page): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByArtistID');
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/artist/' + id, {params: {page: page}});
  }

  /**
   * Finds Events filtered by:
   * @param eventName of the event
   * @param eventType of the event
   * @param artistName artist which takes part in the event
   * @param content of the event
   * @param description of the event
   * @param page the number of the page to return
   */
  public findEventsFilteredByAttributes(eventName, eventType, artistName, content, description, page): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByAttributes');
    let parameters = new HttpParams();
    parameters = eventName ? parameters.append('eventName', eventName) : parameters;
    parameters = content ? parameters.append('content', content) : parameters;
    parameters = artistName ? parameters.append('artistName', artistName) : parameters;
    parameters = description ? parameters.append('description', description) : parameters;
    parameters = eventType ? parameters.append('eventType', eventType) : parameters;
    parameters = parameters.append('page', page);
    return this.httpClient.get<Event[]>(this.eventBaseUri, { params: parameters });
  }
}
