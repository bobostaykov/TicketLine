import { Injectable } from '@angular/core';
import {ResultsFor} from '../../../datatype/results_for';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../../../dtos/show';

@Injectable({
  providedIn: 'root'
})
export class ShowService {

  private showBaseUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get all shows that correspond to a particular type of filter (resultsFor: LOCATION, ARTIST, EVENT), with a specific name or id
   * If resultsFor === ResultsFor.LOCATION, name_or_id will contain the id of the location, otherwise the name of the artist/event
   */
  /*
  public findShows(resultsFor: ResultsFor, nameOrId: string): Observable<Show[]> {
    return this.httpClient.get<Show[]>(this.showBaseUri, {params: { results_for: ResultsFor[resultsFor], name_or_id: nameOrId }});
  }
  */
  public findShowsFilteredByEventName(eventName): Observable<Show[]> {
    console.log('ShowService: findShowsFilteredByEventName');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/event', {params: {eventName: eventName}});
  }

  public findShowsFilteredByLocationID(id): Observable<Show[]> {
    console.log('ShowService: findShowsFilteredByLocationID');
    console.log('Test: ' + '/location/${id}');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/location/${id}');
  }

  public findShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, duration) {
    console.log('ShowService: findShowsFilteredByShowAttributes');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', {
      params: {
        eventName: eventName, hallName: hallName, dateFrom: dateFrom, dateTo: dateTo, timeFrom: timeFrom, timeTo: timeTo,
        minPrice: minPrice, maxPrice: maxPrice, duration: duration
      }
    });
  }
}
