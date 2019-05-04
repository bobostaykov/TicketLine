import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get top ten events from backend
   */
  getTopTenEvents(): Observable<Event[]> {
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/topten');
  }

}
