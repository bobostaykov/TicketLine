import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../../../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventResultsService {

  private eventBaseUri: string = this.globals.backendUri + '/еvents';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findEventsFilteredByAttributes(eventName, eventType, content, description): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByAttributes');
    return this.httpClient.get<Event[]>(this.eventBaseUri, {
      params: {
        eventName: eventName, eventType: eventType, content: content, description: description
      }
    });
  }
}
