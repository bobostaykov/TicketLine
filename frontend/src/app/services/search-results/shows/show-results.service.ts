import { Injectable } from '@angular/core';
import {ResultsFor} from '../../../datatype/results_for';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../../../dtos/show';
import {Artist} from '../../../dtos/artist';

@Injectable({
  providedIn: 'root'
})
export class ShowResultsService {

  private showBaseUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Deletes specified show in backend
   * @param showId of the show to delete
   */
  public deleteShow(showId: number): Observable<{}> {
    console.log('ShowResultsService: deleteShow');
    return this.httpClient.delete(this.showBaseUri + '/' + showId);
  }

  /**
   * Updates specified show in backend
   * @param show show dto with changed attributes
   */
  public updateShow(show: Show): Observable<{}> {
    console.log('ShowResultsService: updateShow');
    return this.httpClient.put<Artist>(this.showBaseUri + '/' + show.id, show);
  }

  /**
   * Search for shows filtered by event name
   * @param eventName of the event
   * @param page the number page requested
   */
  public findShowsFilteredByEventName(eventName, page): Observable<Show[]> {
    console.log('ShowResultsService: findShowsFilteredByEventName');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', {params: {eventName: eventName, page: page}});
  }

  /**
   * Search for shows filtered by location id
   * @param id of the location to filter the show by
   * @param page the number page requested
   */
  public findShowsFilteredByLocationID(id, page): Observable<Show[]> {
    console.log('ShowResultsService: findShowsFilteredByLocationID');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/location/' + id, {params: {page: page}});
  }

  /**
   * Search for show filtered by the following parameters
   * @param eventName name of the event to which the show belongs
   * @param eventType type of the event to which the show belongs
   * @param artistName name of the artist participating at that event
   * @param hallName name of the hall in which the show takes place
   * @param dateFrom of the show
   * @param dateTo of the show
   * @param timeFrom of the show
   * @param timeTo of the show
   * @param minPrice of the show
   * @param maxPrice of the show
   * @param duration of the show
   * @param locationName name of the location where the show takes place
   * @param country of the location where the show takes place
   * @param city of the location where the show takes place
   * @param street of the location where the show takes place
   * @param postalCode of the location where the show takes place
   * @param page the number page requested
   */
  public findShowsFilteredBySomeParameters(eventName, eventType, artistName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice,
                                           duration, locationName, country, city, street, postalCode, page) {
    console.log('ShowResultsService: findShowsFilteredByShowAttributes');
    let parameters = new HttpParams();
    parameters = eventName ? parameters.append('eventName', eventName) : parameters;
    parameters = eventType ? parameters.append('eventType', eventType) : parameters;
    parameters = artistName ? parameters.append('artistName', artistName) : parameters;
    parameters = hallName ? parameters.append('hallName', hallName) : parameters;
    parameters = dateFrom ? parameters.append('dateFrom', dateFrom) : parameters;
    parameters = dateTo ? parameters.append('dateTo', dateTo) : parameters;
    parameters = timeFrom ? parameters.append('timeFrom', timeFrom) : parameters;
    parameters = timeTo ? parameters.append('timeTo', timeTo) : parameters;
    parameters = minPrice ? parameters.append('minPrice', minPrice) : parameters;
    parameters = maxPrice ? parameters.append('maxPrice', maxPrice) : parameters;
    parameters = duration ? parameters.append('duration', duration) : parameters;
    parameters = locationName ? parameters.append('locationName', locationName) : parameters;
    parameters = country ? parameters.append('country', country) : parameters;
    parameters = city ? parameters.append('city', city) : parameters;
    parameters = street ? parameters.append('street', street) : parameters;
    parameters = postalCode ? parameters.append('postalCode', postalCode) : parameters;
    parameters = parameters.append('page', page);
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', { params: parameters });
  }
}
