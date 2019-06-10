import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Location} from '../../../dtos/location';

@Injectable({
  providedIn: 'root'
})
export class LocationResultsService {

  private locationsBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findLocationsFiltered(country, city, street, postalCode, description, page): Observable<Location[]> {
    console.log('Location Service: findLocationsFiltered');
    let parameters = new HttpParams();
    parameters = country ? parameters.append('country', country) : parameters;
    parameters = city ? parameters.append('city', city) : parameters;
    parameters = street ? parameters.append('street', street) : parameters;
    parameters = postalCode ? parameters.append('postalCode', postalCode) : parameters;
    parameters = description ? parameters.append('postalCode', description) : parameters;
    parameters = parameters.append('page', page);
    return this.httpClient.get<Location[]>(this.locationsBaseUri, {params: parameters});
  }

  public getCountriesOrderedByName(): Observable<string[]> {
    console.log('Location Service: getCountriesOrderedByName');
    return this.httpClient.get<string[]>(this.locationsBaseUri + '/countries');
  }
}
