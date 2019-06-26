import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {EventTickets} from '../../dtos/event_tickets';
import {Event} from '../../dtos/event';
import {TopTenDetails} from '../../dtos/top-ten-details';
import {Customer} from '../../dtos/customer';

// TODO Combine this EventService with EventResultsService

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


  /**
   * Get all events from backend
   */
  public getAllEvents(page: number): Observable<Event[]> {
    console.log('Get all events');
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/?page=' + page);
  }

  /**
   * Deletes the event with the specified id
   * @param eventId id of event to delete
   */
  public deleteEvent(eventId: number): Observable<{}> {
    console.log('Delete event with id ' + eventId);
    return this.httpClient.delete(this.eventBaseUri + '/' + eventId);
  }

  /**
   * updates specified event in backend
   * @param event updated event dto
   */
  public updateEvent(event: Event): Observable<Event> {
    console.log('Update event with id ' + event.id + ' to ' + JSON.stringify(event));
    return this.httpClient.put<Event>(this.eventBaseUri + '/' + event.id, event);
  }

}
