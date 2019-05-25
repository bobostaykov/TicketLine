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

  // OK
  public findEventsFilteredByArtistID(id): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByArtistID');
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/artist/' + id);
  }

  // OK
  public findEventsFilteredByAttributes(eventName, eventType, content, description): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByAttributes');
    return this.httpClient.get<Event[]>(this.eventBaseUri, {
      params: {
        eventName: eventName, eventType: eventType, content: content, description: description
      }
    });
  }
}
