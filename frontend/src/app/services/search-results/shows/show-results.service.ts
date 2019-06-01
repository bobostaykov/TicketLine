import { Injectable } from '@angular/core';
import {ResultsFor} from '../../../datatype/results_for';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Show} from '../../../dtos/show';

@Injectable({
  providedIn: 'root'
})
export class ShowResultsService {

  private showBaseUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findShowsFilteredByEventName(eventName): Observable<Show[]> {
    console.log('ShowResultsService: findShowsFilteredByEventName');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/event', {params: {eventName: eventName}});
  }

  public findShowsFilteredByLocationID(id): Observable<Show[]> {
    console.log('ShowResultsService: findShowsFilteredByLocationID');
    return this.httpClient.get<Show[]>(this.showBaseUri + '/location/' + id);
  }

  // TODO sending DataTypes instead of strings?
  public findShowsFilteredByShowAttributes(eventName, hallName, dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice,
                                           duration, country, city, street, postalCode) {
    console.log('ShowResultsService: findShowsFilteredByShowAttributes');
    let parameters = new HttpParams();
    parameters = eventName ? parameters.append('eventName', eventName) : parameters;
    parameters = hallName ? parameters.append('hallName', hallName) : parameters;
    parameters = dateFrom ? parameters.append('dateFrom', dateFrom) : parameters;
    parameters = dateTo ? parameters.append('dateTo', dateTo) : parameters;
    parameters = timeFrom ? parameters.append('timeFrom', timeFrom) : parameters;
    parameters = timeTo ? parameters.append('timeTo', timeTo) : parameters;
    parameters = minPrice ? parameters.append('minPrice', minPrice) : parameters;
    parameters = maxPrice ? parameters.append('maxPrice', maxPrice) : parameters;
    parameters = duration ? parameters.append('duration', duration) : parameters;
    parameters = country ? parameters.append('country', country) : parameters;
    parameters = city ? parameters.append('city', city) : parameters;
    parameters = street ? parameters.append('street', street) : parameters;
    parameters = postalCode ? parameters.append('postalCode', postalCode) : parameters;
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', { params: parameters });
  }
}
