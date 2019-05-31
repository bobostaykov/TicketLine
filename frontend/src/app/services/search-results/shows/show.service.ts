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
    return this.httpClient.get<Show[]>(this.showBaseUri + '/filter', {
      params: {
        eventName: eventName, hallName: hallName, dateFrom: dateFrom, dateTo: dateTo, timeFrom: timeFrom, timeTo: timeTo,
        minPrice: minPrice, maxPrice: maxPrice, duration: duration, country: country, city: city, street: street, postalCode: postalCode
      }
    });
  }
}
