import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../../../dtos/show';
import {Artist} from '../../../dtos/artist';
import {ShowRequestParameter} from '../../../datatype/requestParameters/ShowRequestParameter';
import {Customer} from '../../../dtos/customer';

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
    return this.httpClient.put<Show>(this.showBaseUri + '/' + show.id, show);
  }

  /**
   * Adds a show to the data base
   * @param show to persist
   */
  public addShow(show: Show): Observable<{}> {
    console.log('ShowResultsService: addShow');
    return this.httpClient.post<Show>(this.showBaseUri, show);
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
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', {params: parameters});
  }

  /**
   * finds and loads search suggestions for shows from the backend
   * show entities found this way only include the name of the show's event, the show's date and its time attribute
   * @param eventName search parameter. Only shows with event names containing this parameter will be found
   * @param date search parameter. Only shows with dates containing this parameter will be found
   * @param time search parameter. Only shows with time attributes containing this parameter will be found
   */
  public getSearchSuggestions(eventName: string, date: string, time: string): Observable<Show[]> {
    let parameters = new HttpParams();
    parameters = eventName ? parameters.append('eventName', eventName) : parameters;
    parameters = date ? parameters.append('date', date) : parameters;
    parameters = time ? parameters.append('time', time) : parameters;
    return this.httpClient.get<Show[]>(this.showBaseUri + '/suggestions', {params: parameters});
  }

  /**
   * finds and loads a single show from backend by its it
   * @param id of show to be found
   * @param include includes special request parameters
   * if set to tickets the show's hall will include ticketstatus with every seat or sector associated with that hall
   */
  public findOneById(id: number, include: ShowRequestParameter[]): Observable<Show> {
    console.log('Get show with id ' + id + ' and request parameters ' + include.toString());
    const parameters = include ? new HttpParams().set('include', include.toString()) : null;
    return this.httpClient.get<Show>(this.showBaseUri + '/' + id, {params: parameters});
  }
}
